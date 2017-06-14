package zym

import com.alibaba.fastjson.JSON
import org.junit.Test
import zym.units.readFileContentByString

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
}