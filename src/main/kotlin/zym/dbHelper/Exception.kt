package zym.dbHelper

/**
 *数据的所有异常对象
 * create by 2017/6/15.
 * @author zym
 */
class NestedTransactionException(message: String) : RuntimeException(message)

/**
 * 连接已经被使用
 */
class ConnectionIsUsed : RuntimeException()

/**
 * Helper的操作已经结束,此对象不能再进行操作
 */
class OperatorIsEnd(message: String) : RuntimeException(message)

/**
 * 没有激活的事务
 */
class NotActivityTransaction : RuntimeException()
