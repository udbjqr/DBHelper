package zym.dbHelper

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import org.apache.logging.log4j.LogManager
import zym.units.readFileContentByString

private val helpers = HashMap<String, Helper>()

/**
 * 数据库操作对象入口.
 *
 * create by 2017/6/1.
 * @author yimin
 */
object HelperFactory : Map<String, Helper> by helpers {
	private val log = LogManager.getLogger(HelperFactory::class.java.name)

	init {
		log.trace("开始初始化数据库帮助管理对象.")
		val config = JSON.parseObject(readFileContentByString("db.config"))

		config.forEach { t, u -> helpers.put(t, createHelper(u as JSONObject)) }
	}

	private fun createHelper(config: JSONObject): Helper {
		when (config["DBType"]) {
			"MSSQL" -> return MSSQLServer(config)
			"ORACLE" -> return OracleHelper(config)
			"PGSQL" -> return PostgresqlHelper(config)
			"MYSQL" -> return MySql(config)
			"H2" -> return H2(config)
			else -> throw IllegalArgumentException("类型都乱指定，你想闹哪样：${config["DBType"]}，去看db.config文件去。")
		}
	}

	val helper get() = helpers["default"]!!

	fun getHelper(name: String): Helper? {
		return helpers[name]
	}
}
