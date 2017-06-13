package zym.units

/**
 *一个循环的单链表结构.
 *
 * create by 2017/6/13.
 * @author zym
 */
class CircularItem<T>(val item: T) {
	var next: CircularItem<T> = this
}