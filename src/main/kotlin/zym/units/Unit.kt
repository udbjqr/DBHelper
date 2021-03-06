package zym.units

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.util.Loader
import java.io.File
import java.nio.charset.Charset

private val logger = LogManager.getLogger("zym.units")!!


/**
 * 根据给定的编码，从文件中读取字符串.
 *
 * 如果文件未找到，将记录一个异常，但直接返回"".
 * 而不是弹出异常
 *
 * @param fileName 文件名
 * @param charset      指定文件的编码格式
 * *
 * @return 读取到的字符串
 */
fun readFileContentByString(fileName: String, charset: Charset = Charsets.UTF_8): String {
	logger.debug("以 $charset 格式读文件: $fileName。")

	return try {
		val fullPath = Loader.getResource(fileName, Loader::class.java.classLoader).file
		File(fullPath).readText(charset)
	} catch (e: Exception) {
		logger.error("读文件$fileName 出现异常，原因：", e)
		""
	}
}
