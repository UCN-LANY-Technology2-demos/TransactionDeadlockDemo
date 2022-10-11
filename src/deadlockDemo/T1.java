package deadlockDemo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.DataContext;
import model.Order;

public class T1 implements Runnable {

	public void updateCustomerOrderStatus(Connection conn) throws SQLException {

		String updateCustomerOrderStatusSql = "UPDATE Customers SET LatestOrderStatus = 'F' WHERE Id = 1 ";

		Statement updateCustomer = conn.createStatement();

		System.out.println(Thread.currentThread().getName() + " request X lock on CUSTOMERS");

		updateCustomer.executeUpdate(updateCustomerOrderStatusSql);
	}

	public Order selectOrder(Connection conn) throws SQLException {

		Order result = null;

		String selectOrdersSql = "SELECT * FROM Orders WHERE CustomerId = 1 ";

		System.out.println(Thread.currentThread().getName() + " request S lock on CUSTOMERS");

		Statement selectOrders = conn.createStatement();
		ResultSet rows = selectOrders.executeQuery(selectOrdersSql);

		if (rows.next()) {
			result = new Order(rows.getInt(1), // ORDER_ID
					rows.getInt(2), // CUSTOMER_ID
					rows.getDate(3), // DATE
					rows.getFloat(4), // TOTAL
					rows.getString(5) // STATUS
			);
		}

		return result;
	}

	@Override
	public void run() {

		try {
			Connection conn = DataContext.getConnection();
			conn.setAutoCommit(false);

			try {

				DataContext.printSessionInfo(conn);

				updateCustomerOrderStatus(conn);

				Order order = selectOrder(conn);

				System.out.println(order);

				conn.commit();
				
				System.out.println(Thread.currentThread().getName() + " locks released");

			} catch (SQLException e) {
	
				conn.rollback();
				throw e;
				
			} finally {
				
				conn.setAutoCommit(true);
			}
		} catch (SQLException e1) {
 
			System.out.println(Thread.currentThread().getName() + ": ERROR! "+ e1.getMessage());
		}
	}

}
