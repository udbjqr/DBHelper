package zym.dbHelper

import org.junit.Test

/**
 * create by 2017/6/14.

 * @author zym
 */
class HelperFactoryTest{
	@Test fun getDBHelper(){
		val helper = HelperFactory.helper
		println(helper)
	}
}