package zym.dbHelper

import org.apache.logging.log4j.LogManager
import zym.units.CircularItem
import java.sql.DriverManager

typealias JConn = java.sql.Connection
val logPool = LogManager.getLogger(ConnectionPool::class.java.name)!!
//val logConn = LogManager.getLogger(Connection::class.java.name)!!

class ConnectionIsUsed : RuntimeException()

/**
 * 连接池对象.
 * 根据数据库配置文件，每个节点应用一个此对象。
 */
class ConnectionPool(val connStr: String, val userName: String, val password: String, val timedOut: Int, val poolNumber: Int) {
	internal val lock = Object()
	private var currConn: CircularItem<Connection>
	internal var aloneCount = 0

	init {
		currConn = CircularItem(createConnection())
		val first: CircularItem<Connection> = currConn
		for (i in 2..poolNumber) {
			currConn = CircularItem(createConnection(alone = true))
		}
		currConn.next = first
	}

	val size: Int
		get() = poolNumber

	fun getAloneConn(): Connection {
		aloneCount++
		logPool.debug("给出一个单独的连接。目前单独连接数:{}，注意关闭。", aloneCount)
		return createConnection()
	}

	fun getFreeConn(): Connection {
		val temp = currConn
		while (true) {
			synchronized(lock) {
				do {
					if (!currConn.item.isUsed) {
						currConn.item.use()
						return currConn.item
					}
					currConn = currConn.next
				} while (temp != currConn.next)

				logPool.info("所有连接都忙。等待释放连接中！")
				lock.wait()
			}
		}
	}

	@Synchronized fun createConnection(alone: Boolean = false): Connection {
		logPool.info("创建一个新的连接。{}")
		return Connection(DriverManager.getConnection(connStr, userName, password), this, alone)
	}
}


/**
 * 此类为自己的连接对象，委托实际连接对象用户使用.
 *
 */
class Connection(var actualConn: JConn, val pool: ConnectionPool, val alone: Boolean = false) : JConn by actualConn {
	var isUsed = false
		get() = isUsed

	fun use() {
		if (this.isUsed) {
			throw ConnectionIsUsed()
		}

		synchronized(pool.lock) {
			this.isUsed = true
		}
	}

	override fun close() {
		synchronized(pool.lock) {
			this.isUsed = false
			if (alone) {
				pool.aloneCount--
			}
			pool.lock.notifyAll()
		}
	}

}
