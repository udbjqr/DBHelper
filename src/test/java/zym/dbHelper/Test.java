package zym.dbHelper;

import java.sql.SQLException;

/**
 * create by 17/10/21.
 *
 * @author zhengyimin
 */

public class Test {
	@org.junit.Test
	public void test()  {
		Helper helper = JDBCHelperFactory.INSTANCE.getHelper();
		helper.select("*").from("test2").query(set -> {
			try {
				System.out.println(set.getObject(1) + "\t\t" + set.getObject(2));
			} catch (SQLException e) {
				e.printStackTrace();
			}

			return null;
		});
	}

	@org.junit.Test
	public void testGetTableInfo() {
		Helper helper = JDBCHelperFactory.INSTANCE.getHelper();
		helper.getTableInfo("ddd").forEach(System.out::println);
	}
}
