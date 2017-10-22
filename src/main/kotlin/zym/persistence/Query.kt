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
class Query<out R : Persistence>(private val factory: Factory<R>) {
	private val jointSql = Array(Joint.values().size) { "" }

	init {
		jointSql[Joint.FORM.ordinal] = factory.tableName
	}

	/**
	 * 执行一个查询,并返回相应的列表的结果集。
	 */
	fun exec(): List<R> {
		val list = mutableListOf<R>()

		JDBCHelperFactory.helper.query(this.jointSql) {
			list.add(factory.get("id", it.getInt("id")))
		}

		return list
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
	fun form(sql: String): Query<R> {
		jointSql[Joint.FORM.ordinal] = sql
		return this
	}

	/**
	 * 设置group by 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun groupBy(sql: String): Query<R> {
		jointSql[Joint.GROUP_BY.ordinal] = sql
		return this
	}


	/**
	 * 设置order by 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun orderBy(sql: String): Query<R> {
		jointSql[Joint.ORDER_BY.ordinal] = sql
		return this
	}

	/**
	 * 设置select 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun select(sql: String): Query<R> {
		jointSql[Joint.SELECT.ordinal] = sql
		return this
	}

	/**
	 * 设置 having 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun having(sql: String): Query<R> {
		jointSql[Joint.HAVING.ordinal] = sql
		return this
	}

	/**
	 * 设置offset 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun offset(sql: String): Query<R> {
		jointSql[Joint.OFFSET.ordinal] = sql
		return this
	}

	/**
	 * 设置limit 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun limit(sql: String): Query<R> {
		jointSql[Joint.LIMIT.ordinal] = sql
		return this
	}

	/**
	 * 设置where 语句的值。
	 *
	 * 未设置时值为""
	 */
	fun where(sql: String): Query<R> {
		jointSql[Joint.WHERE.ordinal] = sql
		return this
	}
}