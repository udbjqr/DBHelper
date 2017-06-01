package zym.dbHelper

import org.junit.Test

/**
 * create by 2017/6/1.

 * @author yimin
 */
class HelperManagerTest{

	@Test fun testMember(){
		println(HelperFactory.helper)
		println(HelperFactory.getHelper("other"))
		println(HelperFactory["other"])
		println(HelperFactory["default"])
	}

}