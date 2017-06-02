package zym

import com.alibaba.fastjson.JSON
import org.junit.Test

/**
 * create by 2017/6/1.

 * @author yimin
 */
class UnitKtTest {
	@Test fun testLoader() {
		println(readFileContentByString("log4j2-test.xml"))
	}

	@Test fun testJson() {
		val dbConfig = JSON.parseObject(readFileContentByString("db.config"))
		println(dbConfig.getJSONObject("default").getString("DBPoolNum")!!)

	}

	@Test fun testVararg() {
		println(ddd(1,2,3,4,5,6,7,8,9,10))
	}

	private fun ddd(vararg values: Int): Int {
		var sum = 0
		for (i in values.indices) {
			logger.trace("$i \t $sum")
			sum += values[i]
		}

		values.let { println(it.size)  }
		return sum
	}
}