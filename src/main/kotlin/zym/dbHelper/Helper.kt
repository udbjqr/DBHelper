package zym.dbHelper

import com.alibaba.fastjson.JSONObject
import java.sql.ResultSet

interface Helper {
	/**
	 * 执行一个无返回值的SQL操作.
	 *
	 * @return 返回影响的行数
	 */
	fun execute(sql: String, then: () -> Unit, error: (sql: String, e: Exception?) -> Unit): Int

	/**
	 * 执行一条单独的查询语句，必须以select开头，并且将获得数据调用then方法进行处理.
	 * @param then 此方法必须存在，获取的数据集在
	 * @return 返回一个空值
	 */
	fun select(sql: String, then: ((set: ResultSet) -> Unit), error: (sql: String, e: Exception?) -> Unit = { s, e -> Unit }, limit: Int = 0, offset: Int = 0): Unit

	/**
	 * 连续执行多个sql语句，并在每一个语句执行完后调用then方法.
	 *
	 * 这个过程将是一个完整过程，任何其中一个语句执行错误，均将整个失败并执行回滚操作。
	 *
	 * @return 返回最后执行的语句的第一行第一个值
	 */
	fun <T> execBatchSql(sqls: Iterable<String>, then: (set: ResultSet) -> Unit = { Unit }, error: (sql: String, e: Exception?) -> Unit = { s, e -> Unit }): T

	/**
	 * 执行一条单独的查询语句，必须以select开头，并且将获得的第一行第一列的数据返回.
	 */
	fun <T> queryWithOneValue(sql: String, then: (result: T) -> Unit = { Unit }, error: (sql: String, e: Exception?) -> Unit = { s, e -> Unit }): T

	/**
	 * 开始一个事务.
	 *
	 * 此处事务将是单层的，即已经开始了一个事务，无法进行嵌套。
	 * 已经开始过的事务在调用此方法时将直接返回false
	 */
	fun beginTran(): Boolean

	/**
	 * 提交数据操作
	 *
	 * 未开始事务将返回false
	 */
	fun commit(): Boolean

	/**
	 * 回滚操作
	 *
	 * 未开始事务将返回false
	 */
	fun rollback(): Boolean

	/**
	 * 将各种传入值格式化成数据库理解的值
	 */
	fun transition(value: Any): String

	/**
	 * 类似String.format操作，将字符串与值做格式化.
	 *
	 * 采用：%1，%2 这样的格式，允许一个参数多次使用
	 */
	fun format(pattern: String, vararg values: Any): String

}

abstract class AbstractHelper(protected val config: JSONObject) : Helper {
	override fun transition(value: Any): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun format(pattern: String, vararg values: Any): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun execute(sql: String, then: () -> Unit, error: (sql: String, e: Exception?) -> Unit): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun select(sql: String, then: (set: ResultSet) -> Unit, error: (sql: String, e: Exception?) -> Unit, limit: Int, offset: Int) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun <T> execBatchSql(sqls: Iterable<String>, then: (set: ResultSet) -> Unit, error: (sql: String, e: Exception?) -> Unit): T {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun <T> queryWithOneValue(sql: String, then: (result: T) -> Unit, error: (sql: String, e: Exception?) -> Unit): T {
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