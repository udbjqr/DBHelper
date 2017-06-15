package zym.dbHelper

import com.alibaba.fastjson.JSONObject
import org.apache.logging.log4j.LogManager
import java.sql.ResultSet

/**
 * 数据库操作帮助类.
 *
 * 此类设计目标：简化数据库的操作，加速开发效率
 *
 * 针对SQL的数据格式，可使用format方法来格式化，这需要采用类似String.format方式。使用%1，%2.这类写法。
 * 例：
 *   Helper.select(Helper.format("select * from table where a= %1 and %2 = %3","name","id",20),{set -> {set.get()}})
 *
 */
interface Helper {
	/**
	 * 执行一个无返回值的SQL操作.
	 *
	 * @return 返回影响的行数
	 */
	fun execute(sql: String, error: (sql: String, e: Exception?) -> Unit, then: () -> Unit): Int

	/**
	 * 执行一条单独的查询语句，必须以select开头，并且将获得数据调用then方法进行处理.
	 * @param then 此方法必须存在，获取的数据集在
	 * @return 返回一个空值
	 */
	fun select(sql: String, limit: Int = 0, offset: Int = 0, error: (sql: String, e: Exception?) -> Unit = { s, e -> Unit }, then: ((set: ResultSet) -> Unit)): Unit

	/**
	 * 连续执行多个sql语句，并在每一个语句执行完后调用then方法.
	 *
	 * 这个过程将是一个完整过程，任何其中一个语句执行错误，均将整个失败并执行回滚操作。
	 *
	 * @return 返回最后执行的语句的第一行第一个值
	 */
	fun <T> execBatchSql(sqls: Iterable<String>, error: (sql: String, e: Exception?) -> Unit = { s, e -> Unit }, then: (set: ResultSet) -> Unit = { Unit }): T

	/**
	 * 执行一条单独的查询语句，必须以select开头，并且将获得的第一行第一列的数据返回.
	 */
	fun <T> queryWithOneValue(sql: String, error: (sql: String, e: Exception?) -> Unit = { s, e -> Unit }, then: (result: T) -> Unit = { Unit }): T

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
	 */
	fun commit()

	/**
	 * 回滚操作
	 *
	 */
	fun rollback()

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

abstract class AbstractHelper : Helper {
	protected val log = LogManager.getLogger(AbstractHelper::class.java.name)

	protected val DB_SERVER_ADD = "DBServerAdd"
	protected val DB_SERVER_PORT = "DBServerPort"
	protected val DB_DATA_BASE_NAME = "DBDataBaseName"
	protected val DB_PASSWORD = "DBPassword"
	protected val DB_USER_NAME = "DBUserName"
	protected val CONNECTION_TIMEOUT = "ConnectionTimeout"
	protected val DB_POOL_NUM = "DBPoolNum"

	internal var pool: ConnectionPool? = null


	open fun getConnection(): Connection {
		return pool!!.getFreeConn()
	}

	override fun execute(sql: String, error: (sql: String, e: Exception?) -> Unit, then: () -> Unit): Int {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun select(sql: String, limit: Int, offset: Int, error: (sql: String, e: Exception?) -> Unit, then: (set: ResultSet) -> Unit) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun <T> execBatchSql(sqls: Iterable<String>, error: (sql: String, e: Exception?) -> Unit, then: (set: ResultSet) -> Unit): T {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun <T> queryWithOneValue(sql: String, error: (sql: String, e: Exception?) -> Unit, then: (result: T) -> Unit): T {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun beginTran(): Boolean {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun commit() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun rollback() {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun transition(value: Any): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun format(pattern: String, vararg values: Any): String {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}


class MSSQLServer(config: JSONObject) : AbstractHelper()
class MySql(config: JSONObject) : AbstractHelper()
class H2(config: JSONObject) : AbstractHelper()

/**
 * 事务对象.
 * 此对象当用户请求开始一个事务构造,在使用事务期间必须一直执有此对象。
 * 所执行的语句也必须调用此事务内语句。
 * 最后调用此对象的commit或者rollback方法。
 */
class Transaction internal constructor(val helper: AbstractHelper) : AbstractHelper(), Helper by helper {
	val conn = pool!!.getAloneConn()

	override fun getConnection(): Connection {
		return conn
	}

	override fun beginTran(): Boolean {
		throw NestedTransactionExp("目前不允许事务的嵌套")
	}

	override fun commit() {
		conn.commit()
	}

	override fun rollback() {
		return conn.rollback()
	}

}