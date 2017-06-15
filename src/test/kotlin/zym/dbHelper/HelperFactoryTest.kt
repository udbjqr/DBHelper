package zym.dbHelper

import org.junit.Test
import java.sql.Connection
import kotlin.concurrent.thread

/**
 * create by 2017/6/14.

 * @author zym
 */
class HelperFactoryTest {
	@Test fun getDBHelper() {
		val helper = HelperFactory.helper
		println(helper)
	}

	@Test fun testgetAloneConnection() {
		val helper = HelperFactory.helper
		if (helper is PostgresqlHelper) {
			val pool = helper.pool
			val aloneConn: Connection = pool!!.getAloneConn()
			assert(pool.aloneCount == 1)
			aloneConn.close()
			assert(pool.aloneCount == 0)
		}
	}

	@Test fun testgetConnection() {
		val conns = ArrayList<Connection>()

		thread(start = true) {
			while (true) {
				if (conns.isEmpty()) {
					Thread.sleep(1000)
				}
				val conn = conns.removeAt(0)
				conn.close()
				logPool.debug("释放连接，$conn")
				Thread.sleep(100)
			}
		}

		val helper = HelperFactory.helper

		if (helper is PostgresqlHelper) {
			val pool = helper.pool
			for (i in 1..99) {
				val free = pool!!.getFreeConn()
				conns.add(free)
				logPool.debug("得到连接，$free")
			}
		}
	}

}