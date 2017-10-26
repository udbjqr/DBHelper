package zym.dbHelper

import com.alibaba.fastjson.JSONObject
import java.util.*
import kotlin.collections.ArrayList


/**
 *
 * create by 2017/6/1.
 * @author yimin
 */
class PostgreSql(trim: String, connection: Connection) : AbstractJDBCHelper(connection) {
	private val trim = "$$trim$"

	override fun format(value: Any?): String {
		if (value == null) return "null"

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

	override fun getTableInfo(tableName: String): List<Field> {
		val sql = "WITH class AS (SELECT to_regclass('$tableName') :: OID AS id), pk AS (select attname from pg_attribute where attrelid = (SELECT indexrelid AS id FROM pg_index WHERE indrelid = (select id from class)))SELECT a.attname, a.atttypid, t.typname, a.attndims, a.atttypmod, a.attnotnull, pg_get_expr(ad.adbin, a.attrelid), des.description, exists(select 1 from pk where pk.attname = a.attname) as ispk, pg_get_expr(ad.adbin, a.attrelid)~\$a\$^nextval\\('.{1,}'::regclass\\)\$\$a\$ AS isSequ FROM pg_attribute a INNER JOIN pg_type t ON a.atttypid = t.oid LEFT JOIN pg_attrdef ad ON a.attnum = ad.adnum AND a.attrelid = ad.adrelid LEFT JOIN pg_description des ON a.attrelid = des.objoid AND a.attnum = des.objsubid WHERE a.attrelid = (select id from class) AND a.attnum > 0 AND attisdropped = FALSE"

		val structure = ArrayList<Field>()
		query(sql) {
			val type = when (it.getInt("atttypid")) {
				1043 -> ColumnType.VARCHAR
				23 -> ColumnType.INT4
				1114 -> ColumnType.TIMESTAMP
				1007 -> ColumnType.INT4_ARRAY
				3802, 114 -> ColumnType.JSON
				790 -> ColumnType.MONEY
				1015 -> ColumnType.VARCHAR_ARRAY
				1700 -> ColumnType.NUMERIC
				2950 -> ColumnType.UUID
				16 -> ColumnType.BOOLEAN
				1005 -> ColumnType.INT2_ARRAY
				20 -> ColumnType.INT8
				else -> ColumnType.UNKNOWN
			}

			structure.add(Field(it.getString("attname"), type, it.getBoolean("attnotnull"), it.getString("pg_get_expr") ?: "", it.getString("description") ?: "", it.getBoolean("ispk"), it.getBoolean("isSequ")))
		}

		return structure
	}
}
