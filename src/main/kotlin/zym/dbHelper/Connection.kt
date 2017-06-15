package zym.dbHelper

import org.apache.logging.log4j.LogManager
import zym.units.CircularItem
import java.sql.DriverManager

typealias JConn = java.sql.Connection
val logPool = LogManager.getLogger(ConnectionPool::class.java.name)!!
val logConn = LogManager.getLogger(Connection::class.java.name)!!

class ConnectionIsUsed : RuntimeException()

/**
 * 连接池对象.
 * 根据数据库配置文件，每个节点应用一个此对象。
 */
class ConnectionPool(val connStr: String, val userName: String, val password: String, var timedOut: Long, val poolNumber: Int) {
	internal val lock = Object()
	private var currConn: CircularItem<Connection>
	internal var aloneCount = 0

	init {
		if (poolNumber < 1) {
			throw IndexOutOfBoundsException("连接池内连接数量必须大于等于1")
		}

		timedOut *= 1000 //将超时时间修改成毫秒
		currConn = CircularItem(createConnection())
		val first: CircularItem<Connection> = currConn
		for (i in 2..poolNumber) {
			currConn = CircularItem(createConnection(), currConn)
		}
		first.next = currConn
	}

	val size: Int
		get() = poolNumber

	fun getAloneConn(): Connection {
		aloneCount++
		logPool.debug("给出一个单独的连接。目前单独连接数:{}，注意关闭。", aloneCount)
		return createConnection(alone = true)
	}

	fun getFreeConn(): Connection {
		val temp = currConn
		while (true) {
			synchronized(lock) {
				do {
					if (!currConn.item.isUsed) {
						return handleIdleReapleConnection(currConn)
					}
					currConn = currConn.next!!
				} while (temp != currConn)

				logPool.info("所有连接都忙。等待释放连接中！")
				lock.wait()
			}
		}
	}

	private fun handleIdleReapleConnection(connWrap: CircularItem<Connection>): Connection {
		//如果连接空闲时间超过指定时间，重新连接
		if (connWrap.item.idleTimestamp + timedOut > System.currentTimeMillis()) {
			connWrap.item.realclose()
			connWrap.item = createConnection()
		}
		connWrap.item.use()
		return connWrap.item
	}

	@Synchronized fun createConnection(alone: Boolean = false): Connection {
		logPool.info("创建一个新的连接。$connStr,alone:$alone")
		return Connection(DriverManager.getConnection(connStr, userName, password), this, alone)
	}
}

/**
 * 此类为自己的连接对象，委托实际连接对象用户使用.
 *
 */
class Connection(var actualConn: JConn, val pool: ConnectionPool, val alone: Boolean = false) : JConn by actualConn {
	var idleTimestamp = System.currentTimeMillis()
		private set

	var isUsed = false
		private set

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
			idleTimestamp = System.currentTimeMillis()
			this.isUsed = false
			if (alone) {
				pool.aloneCount--
				logConn.debug("一个独立的连接关闭。当前连接数：${pool.aloneCount}")
			}
			pool.lock.notifyAll()
		}
	}

	fun realclose() {
		actualConn.close()
	}


}
