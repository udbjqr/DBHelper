package zym.persistence


/**
 *主、从表之间的关联关系表示.
 *
 * @author zhengyimin
 */
data class Relationship(
	val pTableFactory: AbstractFactory<AbstractPersistence>,
	val rTableFactory: AbstractFactory<AbstractPersistence>,
	/**
	 * 第一个String为主表字段名,第二个String为从表字段名
	 */
	val Fields: Array<Pair<String, String>>
) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		return false
	}

	override fun hashCode(): Int {
		var result = pTableFactory.hashCode()
		result = 31 * result + rTableFactory.hashCode()

		return result
	}


}
