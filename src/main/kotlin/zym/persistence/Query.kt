package zym.persistence

import zym.dbHelper.JDBCHelperFactory
import zym.dbHelper.Joint
import java.sql.ResultSet

/**
 * 持久化对象的查询对象.
 *
 * 此对象由持久化工厂对象获得.
 * 每个查询对象可以多次获取数据并将之传出。
 * 每个查询对象对应一个工厂对象
 */
class Query<out T : Persistence>(private val factory: Factory<T>) {
	private val jointSql = Array(Joint.values().size) { "" }

	init {
		jointSql[Joint.FORM.ordinal] = factory.tableName
	}

	/**
	 * 根据传入的sql语句获得对象.
	 *
	 * 注意:此对象必须能够返回当前的类型的值。否则将
	 */
	fun exec(sql: String): List<T> {
		val list = mutableListOf<T>()

		JDBCHelperFactory.helper.query(sql) {
			readResultSet(it, list)
		}

		return list
	}

	private fun readResultSet(set: ResultSet, list: MutableList<T>) {
		val item: T = factory.getNewObject()
		(item as AbstractPersistence).setFieldDataByDB(set)
		list.add(item)
	}

	/**
	 * 执行一个查询,并返回相应的列表的结果集。
	 */
	fun exec(): List<T> {
		val list = mutableListOf<T>()

		JDBCHelperFactory.helper.query(this.jointSql) {
			readResultSet(it, list)
		}

		return list
	}

	/**
	 * 根据传入的sql语句获得对象.
	 *
	 * 此方法可执行任意查询操作，根据返回的结果集自行处理。
	 *
	 * 此方法可执行与指定的对象毫无关系的查询，建议使用DBHelper的查询方法
	 */
	fun exec(sql: String, then: (ResultSet) -> Unit) {
		JDBCHelperFactory.helper.query(sql, then)
	}

	/**
	 * 执行查询,并且直接处理,不返回对应对象
	 *
	 * 此处的参数无需调用ResultSet.next()方法。方法体内会自动调用.
	 */
	fun exec(then: (ResultSet) -> Unit) {
		JDBCHelperFactory.helper.query(this.jointSql, then)
	}

	/**
	 * 设置form 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun form(sql: String): Query<T> {
		jointSql[Joint.FORM.ordinal] = sql
		return this
	}

	/**
	 * 设置group by 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun groupBy(sql: String): Query<T> {
		jointSql[Joint.GROUP_BY.ordinal] = sql
		return this
	}


	/**
	 * 设置order by 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun orderBy(sql: String): Query<T> {
		jointSql[Joint.ORDER_BY.ordinal] = sql
		return this
	}

	/**
	 * 设置select 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun select(sql: String): Query<T> {
		jointSql[Joint.SELECT.ordinal] = sql
		return this
	}

	/**
	 * 设置 having 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun having(sql: String): Query<T> {
		jointSql[Joint.HAVING.ordinal] = sql
		return this
	}

	/**
	 * 设置offset 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun offset(sql: String): Query<T> {
		jointSql[Joint.OFFSET.ordinal] = sql
		return this
	}

	/**
	 * 设置limit 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun limit(sql: String): Query<T> {
		jointSql[Joint.LIMIT.ordinal] = sql
		return this
	}

	/**
	 * 设置where 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun where(sql: String): Query<T> {
		jointSql[Joint.WHERE.ordinal] = sql
		return this
	}
}