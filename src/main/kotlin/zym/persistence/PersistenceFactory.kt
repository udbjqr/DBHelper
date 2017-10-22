package zym.persistence

/**
 * 持久化对象实例的工厂接口.
 * <p>
 * 每一个对象均会实现一个继承类，用以获得对象实例。
 * <p>
 * 实现类做为一个单例存在，并且在构造函数当中设置对象类的表名/字段名等信息。并且实现{@link #createObject(User)}方法，
 * 并返回相对应的对象实例。
 * <p>
 * 使用此对象设置的表必须有主键，并且使用{@link #addField(String, Class, Object, boolean, boolean)}方法设置字段主键。
 * <p>
 * 继承类可修改对象操作过程当中执行的语句，{@link #setWherePrimaryKeys()}方法默认被其他方法调用。
 * <p>
 * 调用{@link #getObject(String, Object)}方法得到一个存在的对象。如此对象不存在，将返回null.调用{@link #getNewObject(User)}
 * 方法得到一个新的对象。
 * <p>
 * 每次获得一个对象时会将此对象进行缓存，使用{@link #setIsCheck(boolean)}可设置是否需要缓存，默认为true.
 */
interface Factory<out T : Persistence> {
	val tableName: String

	//	fun getTableName(): String
	fun createQuery(): Query<T>

	fun get(name: String, value: Any): T?

	fun createNewObject(): T
}

class PersistenceFactory<out T : Persistence>(override val tableName: String) :Factory<T>{
	override fun get(name: String, value: Any): T? {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createNewObject(): T {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun createQuery(): Query<T> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

}

