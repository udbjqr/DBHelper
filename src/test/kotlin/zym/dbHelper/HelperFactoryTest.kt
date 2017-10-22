package zym.dbHelper

import org.junit.Test
import kotlin.concurrent.thread

/**
 * create by 2017/6/14.

 * @author zym
 */
class HelperFactoryTest {

	@Test
	fun getDBHelper() {
		thread {
			while (true) {
				val helper = JDBCHelperFactory.helper
				helper.query("select count(*) from test;") {
					println("${it.getObject(1)}")
				}
			}
		}

		thread(start = true) {
			while (true) {
				val helper = JDBCHelperFactory.helper
				helper.execute("insert into test(name) values('aaaaaa')")
			}
		}

		val lock = Object()
		synchronized(lock) {
			lock.wait()
			Thread().join()
		}
	}


	@Test
	fun testIns() {
		val helper = JDBCHelperFactory.helper
		helper.select("*").from("test2").query {
			logPool.info("${it.getObject(1)}\t\t${it.getObject(2)}")
		}
	}
}