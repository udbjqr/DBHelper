package zym.dbHelper

import com.alibaba.fastjson.JSONObject
import java.util.*


/**
 *
 * create by 2017/6/1.
 * @author yimin
 */
class PostgreSql(trim: String, connection: Connection) : AbstractJDBCHelper(connection) {
	private val trim = "$$trim$"

	override fun format(value: Any): String {
		return when (value) {
			is String, is Int -> "$trim$value$trim"
			is Date -> "to_timestamp(${value.time})"
			is JSONObject -> "$trim$value$trim::jsonb"
			is Array<*> -> buildString {
				append("$trim{")
				value.forEach { append("$it,") }
				delete(length - 1, length)
				append("}$trim")
			}
			else -> "$trim$value$trim"
		}
	}
}
