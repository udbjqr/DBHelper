package zym

import org.junit.Test
import zym.persistence.AbstractFactory
import zym.persistence.AbstractPersistence
import zym.units.readFileContentByString

/**
 * create by 2017/6/1.

 * @author yimin
 */
class UnitKtTest {
	@Test
	fun testLoader() {
		println(readFileContentByString("log4j2-test.xml"))
	}

	@Test
	fun testJson() {

	}

	@Test
	fun testObject() {
		val aa = aaaF.get("id", 609)!!
		println("name:" + aa["name"] + " id:" + aa["id"])
	}
}


class Aaa : AbstractPersistence(aaaF) {
	override fun completeReadFromDB() {

	}

}

object aaaF : AbstractFactory<Aaa>("test") {
	override fun createNewObject(): Aaa {
		return Aaa()
	}

}