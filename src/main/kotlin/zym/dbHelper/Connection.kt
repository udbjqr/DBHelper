package zym.dbHelper

import org.apache.logging.log4j.LogManager
import java.sql.DriverManager

typealias JConn = java.sql.Connection

internal val logPool = LogManager.getLogger(ConnectionPool::class.java.name)!!


/**
 * 连接池对象.
 * 根据数据库配置文件，每个连接应用一个此对象。
 */
class ConnectionPool(private val connStr: String, private val userName: String, private val password: String, private var timedOut: Long, private val poolNumber: Int, helper: (Connection) -> Helper) {
	internal val lock = Object()
	private var currConn: CircularItem
	private var getHelper = helper

	@Suppress("unused")
	val size: Int
		get() = poolNumber

	init {
		if (poolNumber < 1) {
			throw IndexOutOfBoundsException("连接池内连接数量必须大于等于1")
		}
		timedOut *= 1000 //将超时时间修改成毫秒

		currConn = createCircularItem()
		val first: CircularItem = currConn
		for (i in 2..poolNumber) {
			currConn = createCircularItem(currConn)
		}

		first.next = currConn
	}

	private fun createCircularItem(next: CircularItem? = null): CircularItem {
		val conn = createConnection()
		return CircularItem(conn, getHelper(conn), next)
	}

	fun getFreeConn(): CircularItem {
		val temp = currConn
		while (true) {
			synchronized(lock) {
				do {
					if (!currConn.item.isUsed) {
						handleIdleReplaceConnection(currConn)
						currConn.item.use()
						return currConn
					}

					currConn = currConn.next!!
				} while (temp != currConn)

				logPool.info("所有连接都忙。等待释放连接中！")
				lock.wait()
			}
		}
	}

	private fun handleIdleReplaceConnection(connWrap: CircularItem) {
		//如果连接空闲时间超过指定时间，重新连接
		if (connWrap.item.idleTimestamp + timedOut < System.currentTimeMillis()) {
			logPool.debug("超时,换连接。上次使用时间:${connWrap.item.idleTimestamp},超时时间:$timedOut,当前系统时间:${System.currentTimeMillis()}")
			connWrap.item.realClose()
			connWrap.item = createConnection()
		}

		connWrap.helper = getHelper(connWrap.item)
	}

	private fun createConnection(): Connection {
		logPool.debug("创建一个新的连接。$connStr")
		return Connection(DriverManager.getConnection(connStr, userName, password), this)
	}
}

/**
 * 此类为自己的连接对象，委托至实际连接对象.
 *
 */
class Connection(private var actualConn: JConn, private val pool: ConnectionPool) : JConn by actualConn {
	var idleTimestamp = System.currentTimeMillis()
		private set

	var isUsed = false
		private set

	override fun isClosed(): Boolean {
		return !isUsed || actualConn.isClosed
	}

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
			autoCommit = true

			pool.lock.notifyAll()
		}
	}

	fun realClose() {
		actualConn.close()
	}
}
