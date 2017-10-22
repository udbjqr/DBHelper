package zym.persistence

/**
 * 表明一个可以操作的实例化的对象,此对象与数据库表记录相对应.
 *
 * 实现此接口的对象，表明可以直接对持久层做读写操作.
 * 每一个实现此接口的对象代表一行记录。
 *
 * 此接口的实现类可自行处理对应的持久层保存/读取/更新。保证与持久层的数据记录一致。
 *
 * 实例对象已经实例化，新增加与获取从工厂类当中获取。
 */
interface Persistence {
	/**
	 * 得到指定名称的数据的值.
	 *
	 * @param name 指定返回值的名称。
	 * @param <T>  指定返回值的类型
	 * @return 如果名称未指定，返回null
	</T> */
	operator fun <T> get(name: String): T

	/**
	 * 得到指定名称的数据的值.
	 *
	 * @param name         指定返回值的名称。
	 * @param defaultValue 如果得到的值为NULL.返回默认值。
	 * @param <T>          指定返回值的类型
	 * @return 如果名称未指定，返回null
	</T> */
	operator fun <T> get(name: String, defaultValue: T): T

	/**
	 * 将一个数据写入指定名称的值当中
	 *
	 * @param name  指定的列名称
	 * @param value 要更新的值
	 * @param <T>   值的类型。
	 */
	operator fun <T> set(name: String, value: T)

	/**
	 * 将一个数据写入指定名称的值当中
	 *
	 * @param field  指定的列对象
	 * @param value 要更新的值
	 * @param <T>   值的类型。
	</T> */
	operator fun <T> set(field: Field, value: T)

	/**
	 * 刷新数据至持久层.
	 *
	 *
	 * 此方法为底层方法。强制刷新持久层数据
	 *
	 * @return true 成功，false 失败。
	 */
	fun flush(): Boolean

	/**
	 * 将此接口代表的数据删除.
	 *
	 *
	 * 具体的删除实现由实现类自己实现。
	 *
	 * @return 成功：true，失败，false
	 */
	fun delete(): Boolean

	/**
	 * 从数据当中获取指定的数据列的索引值,并回填至对象当中.
	 *
	 * 此方法一般用与在需要立即得到ID值并继续执行后续动作时使用。
	 *
	 * 执行此方法并未将数据进行持久化动作
	 *
	 * @param columnName
	 */
	fun loadSerialToColumn(columnName :String = "id",seq :String? = null)
}