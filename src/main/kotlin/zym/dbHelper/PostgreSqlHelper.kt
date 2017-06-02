package zym.dbHelper

import com.alibaba.fastjson.JSONObject

/**
 *
 * create by 2017/6/1.
 * @author yimin
 */
class PostgreSqlHelper(config: JSONObject) : AbstractHelper(config) {
	private val pool: ConnectionPool

	private val STR_INCLUDE: String

	init {
		Class.forName("org.postgresql.Driver")
		STR_INCLUDE = "\$${config["trim"]}\$"

		val connectionUrl = "jdbc:postgresql://${config[DB_SERVER_ADD]}:${config[DB_SERVER_PORT]}/${config[DB_DATA_BASE_NAME]}"
		pool = ConnectionPool(connectionUrl, config.getString(DB_USER_NAME), config.getString(DB_PASSWORD), config.getInteger(CONNECTION_TIMEOUT), config.getInteger(DB_POOL_NUM))
	}


}
