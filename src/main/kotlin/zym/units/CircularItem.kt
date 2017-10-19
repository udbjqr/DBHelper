package zym.units

import zym.dbHelper.Connection
import zym.dbHelper.Helper

/**
 *一个循环的单链表结构.
 *
 * create by 2017/6/13.
 * @author zym
 */
class CircularItem(var item: Connection, var helper: Helper, var next: CircularItem? = null)