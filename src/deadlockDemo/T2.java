package deadlockDemo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import database.DataContext;
import model.Customer;

public class T2 implements Runnable {

	public void insertOrder(Connection conn) throws SQLException {
		String sql = "INSERT INTO Orders VALUES (1, GETDATE(), 100, 'A')";

		Statement insertStatement = conn.createStatement();

		System.out.println(Thread.currentThread().getName() + " request X lock on ORDERS");

		insertStatement.execute(sql);
	}

	public Customer selectCustomer(Connection conn) throws SQLException {

		String sql = "SELECT * FROM Customers WHERE Id = 1";
		Customer customer = null;

		Statement selectStatement = conn.createStatement();

		System.out.println(Thread.currentThread().getName() + " request S lock on CUSTOMERS");

		ResultSet record = selectStatement.executeQuery(sql);

		if (record.next()) {

			customer = new Customer(record.getInt(1), // Id
					record.getString(2), // Name
					record.getString(3) // LatestOrderStatus
			);
		}
				
		return customer;
	}

	public void updateCustomer(Connection conn) throws SQLException {
		String sql = "UPDATE Customers SET LatestOrderStatus = 'A' WHERE Id = 1";

		Statement updateStatement = conn.createStatement();

		System.out.println(Thread.currentThread().getName() + " request X lock on CUSTOMERS");

		updateStatement.execute(sql);
	}

	@Override
	public void run() {

		try {
			Connection conn = DataContext.getConnection();
			conn.setAutoCommit(false);

			try {

				DataContext.printSessionInfo(conn);

				insertOrder(conn);

				Customer customer = selectCustomer(conn);

				System.out.println(customer);

				updateCustomer(conn);

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
