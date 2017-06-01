package zym.databaseConnection

import org.apache.logging.log4j.LogManager
import java.sql.Connection

/**
 *
 * create by 2017/6/1.
 * @author yimin
 */

/**
 * 连接池对象，单例
 */
class ConnectionPool {
	val log = LogManager.getLogger(ConnectionPool::class.java.name)

	companion object Manager{

	}

	init {
	}

}


/**
 * 此类为自己的连接对象，委托实际连接对象用户使用.
 *
 */
class Connection(val actualConn: Connection) : Connection by actualConn {
	val log = LogManager.getLogger(Connection::class.java.name)
}