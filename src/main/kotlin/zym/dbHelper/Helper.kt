package zym.dbHelper

import com.alibaba.fastjson.JSONObject
import java.sql.ResultSet

interface Helper {
	fun execute(sql: String, then: () -> Unit, error: (sql: String, e: Exception?) -> Unit): Int
	fun query(sql: String, then: (set: ResultSet) -> Unit, error: (sql: String, e: Exception?) -> Unit): Unit
	fun <T> queryOneValues(sql: String, then: (result: T) -> Unit, error: (sql: String, e: Exception?) -> Unit): T
	fun beginTran(): Boolean
	fun commit(): Boolean
	fun rollback(): Boolean


}

abstract class AbstractHelper(protected val config: JSONObject) : Helper {
	override fun execute(sql: String, then: () -> Unit, error: (sql: String, e: Exception?) -> Unit): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun query(sql: String, then: (set: ResultSet) -> Unit, error: (sql: String, e: Exception?) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun <T> queryOneValues(sql: String, then: (result: T) -> Unit, error: (sql: String, e: Exception?) -> Unit): T {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun beginTran(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun commit(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun rollback(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}


class MSSQLServer(config: JSONObject) : AbstractHelper(config) {}
class MySql(config: JSONObject) : AbstractHelper(config) {}
class H2(config: JSONObject) : AbstractHelper(config) {}