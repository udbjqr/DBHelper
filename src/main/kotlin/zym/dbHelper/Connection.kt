package zym.dbHelper

import org.apache.logging.log4j.LogManager
import java.sql.DriverManager


val logPool = LogManager.getLogger(ConnectionPool::class.java.name)
val logConn = LogManager.getLogger(Connection::class.java.name)
/**
 *
 * create by 2017/6/1.
 * @author yimin
 */

/**
 * 连接池对象，单例
 */
class ConnectionPool(val connStr: String, val userName: String, val password: String, val timedOut: Int, val poolNumber: Int) {
	@Synchronized fun getFreeConn(): zym.dbHelper.Connection {
		TODO()
		Connection(DriverManager.getConnection(connStr, userName, password))
	}

}


/**
 * 此类为自己的连接对象，委托实际连接对象用户使用.
 *
 */
class Connection(var actualConn: java.sql.Connection) : Connection by actualConn {

}