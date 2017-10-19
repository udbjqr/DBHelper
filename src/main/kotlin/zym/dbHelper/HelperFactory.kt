package zym.dbHelper

import com.alibaba.fastjson.JSONObject
import org.apache.logging.log4j.LogManager
import zym.units.readFileContentByString

internal const val DB_SERVER_ADD = "DBServerAdd"
internal const val DB_SERVER_PORT = "DBServerPort"
internal const val DB_DATA_BASE_NAME = "DBDataBaseName"
internal const val DB_PASSWORD = "DBPassword"
internal const val DB_USER_NAME = "DBUserName"
internal const val CONNECTION_TIMEOUT = "ConnectionTimeout"
internal const val DB_POOL_NUM = "DBPoolNum"

/**
 * 数据库操作对象入口.
 *
 * create by 2017/6/1.
 * @author yimin
 */
object JDBCHelperFactory {
	private val log = LogManager.getLogger(JDBCHelperFactory::class.java.name)
	private val config: JSONObject
	private val connectionPools: HashMap<String, ConnectionPool> = HashMap()
	private val defaultPools: ConnectionPool

	init {
		log.trace("开始初始化数据库帮助管理对象.")
		config = JSONObject.parseObject(readFileContentByString("db.config"))
		config.forEach { t, u -> connectionPools.put(t, createHelper(u as JSONObject)) }

		defaultPools = connectionPools["default"]!!
	}

	private fun createHelper(config: JSONObject): ConnectionPool {
		return when (config["DBType"]) {
			"MSSQL" -> createMSSQLServerPools(config)
			"ORACLE" -> createOraclePools(config)
			"PGSQL" -> createPostgresqlPools(config)
			"MYSQL" -> createMySqlPools(config)
			"H2" -> createH2Pools(config)
			else -> throw IllegalArgumentException("类型都乱指定，你想闹哪样：${config["DBType"]}，去看db.config文件。")
		}
	}

	private fun createH2Pools(config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createMySqlPools(config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createOraclePools(config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createMSSQLServerPools(config: JSONObject): ConnectionPool {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	private fun createPostgresqlPools(config: JSONObject): ConnectionPool {
		Class.forName("org.postgresql.Driver")

		val connectionUrl = "jdbc:postgresql://${config[DB_SERVER_ADD]}:${config[DB_SERVER_PORT]}/${config[DB_DATA_BASE_NAME]}"
		return ConnectionPool(connectionUrl, config.getString(DB_USER_NAME), config.getString(DB_PASSWORD), config.getLong(CONNECTION_TIMEOUT), config.getInteger(DB_POOL_NUM)) {
			PostgreSql(config["trim"] as String, it)
		}
	}

	val helper get() = defaultPools.getFreeConn().helper

	fun getHelper(name: String): Helper {
		return connectionPools[name]!!.getFreeConn().helper
	}
}
