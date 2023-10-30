package deadlockDemo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import database.DataContext;
import model.Order;

public class Transaction1 extends TransactionBase implements Runnable {

	public Transaction1(int isolationLevel) {
		super(isolationLevel);
		// nothing to see here...
	}

	public void updateCustomerOrderStatus(Connection conn) throws SQLException {

		String updateCustomerOrderStatusSql = "UPDATE Customers SET LatestOrderStatus = 'F' WHERE Id = 1 ";

		Statement updateCustomer = conn.createStatement();

		System.out.println(Thread.currentThread().getName() + " request X lock on CUSTOMERS");

		updateCustomer.executeUpdate(updateCustomerOrderStatusSql);
	}

	public Order selectOrder(Connection conn) throws SQLException {

		Order result = null;

		String selectOrdersSql = "SELECT * FROM Orders WHERE CustomerId = 1 ";

		System.out.println(Thread.currentThread().getName() + " request S lock on ORDERS");

		Statement selectOrders = conn.createStatement();
		ResultSet record = selectOrders.executeQuery(selectOrdersSql);

		while (record.next()) {

			result = new Order(record.getInt(1), // Id
					record.getInt(2), // CustomerId
					record.getDate(3), // Date
					record.getFloat(4), // Total
					record.getString(5) // Status
			);
		}
		record.close();

		return result;
	}

	@Override
	public void run() {

		try {
			Connection conn = DataContext.getConnection(this.isolationLevel);
			conn.setAutoCommit(false);

			try {

				DataContext.printSessionInfo(conn);

				updateCustomerOrderStatus(conn); // X lock on Customers
				
				conn.commit();
				
				Order order = selectOrder(conn); // S lock on Orders

				System.out.println(order);



				System.out.println(Thread.currentThread().getName() + " locks released");

			} catch (SQLException e) {

				conn.rollback();
				throw e;
			}
		} catch (Exception e1) {

			System.out.println(Thread.currentThread().getName() + ": ERROR! " + e1.getMessage());
		}
	}

}
