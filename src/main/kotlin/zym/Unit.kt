package zym

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.core.util.Loader
import org.apache.logging.log4j.core.util.Loader.getResource
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.net.URLConnection

val logger = LogManager.getLogger("zym")!!

object tag {}

/**
 * 根据给定的编码，从文件中读取字符串

 * @param fileName 文件名
 * @param code      指定文件的编码格式
 * *
 * @return 读取到的字符串
 */
fun getProperties(fileName: String, code: String): String? {
	logger.debug("Reading configuration from file name: $fileName")

	val url = Loader.getResource(fileName, tag::class.java.classLoader).file

	try {
		File(url)

	} catch (e: Exception) {
		logger.error("读文件$fileName 出现异常，原因：", e)
		return null
	}
}

fun getProperties(configURL: URL, code: String): String? {
	logger.debug("Reading configuration from URL $configURL")
	val stream: InputStream
	val uConn: URLConnection
	val reader: BufferedReader
	val builder = StringBuilder()

	Loader::getResource()
	try {
		uConn = configURL.openConnection()
		uConn.useCaches = false
		stream = uConn.getInputStream()
		val inputStreamReader = InputStreamReader(stream, code)
		reader = BufferedReader(inputStreamReader)
		var tempString: String
		while ((tempString = reader.readLine()) != null) {
			builder.append(tempString).append(StringUtil.NEWLINE)
		}
		reader.close()

		return builder.toString()
	} catch (e: Exception) {
		logger.error("{} error", configURL, e)
		return null
	}

}
