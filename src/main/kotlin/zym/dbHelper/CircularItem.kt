package zym.dbHelper

/**
 *一个循环的单链表结构.
 *
 * create by 2017/6/13.
 * @author zym
 */
class CircularItem(var item: Connection, var helper: Helper, var next: CircularItem? = null)