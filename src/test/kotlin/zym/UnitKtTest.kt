package zym

import org.apache.logging.log4j.core.util.Loader
import org.junit.Test
/**
 * create by 2017/6/1.

 * @author yimin
 */
class UnitKtTest {
	@Test fun testLoader{
		println( Loader.getResource("log4j2-test.xml", tag::class.java.classLoader).file)
	}
}