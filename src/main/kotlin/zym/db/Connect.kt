package zym.db

import org.apache.logging.log4j.LogManager
import java.sql.Connection

/**
 *
 * create by 2017/5/31.
 * @author yimin
 */
class Connect(val conn: Connection) : Connection by conn {
	private val log = LogManager.getLogger("Connect")

	override fun close() {
		log.trace("完成")
		conn.close()
	}
}


interface A {

	fun close()
	fun other()
}


class B : A {

	private val log = LogManager.getLogger(B::class.java.name)

	override fun close() {
		log.trace("B close")
	}

	override fun other() {
		log.trace("B other")
	}
}


class C(val b: A) : A by b {
	private val log = LogManager.getLogger(C::class.java.name)

	override fun close() {
		log.trace("C close")

		b.close()
	}
}

fun main(args: Array<String>) {
	val c = C(B())

	c.other()
	c.close()
}