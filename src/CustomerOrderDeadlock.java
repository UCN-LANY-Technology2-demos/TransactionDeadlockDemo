import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Customer;
import model.Order;

public class CustomerOrderDeadlock {

	public ArrayList<Order> updateCustomerOrderStatus(int customerId, String orderStatus) {

		Connection conn = Database.getConnection(Connection.TRANSACTION_SERIALIZABLE);

		ArrayList<Order> orders = new ArrayList<Order>();

		PreparedStatement updateCustomer = null;
		PreparedStatement selectOrders = null;

		String updateCustomerOrderStatusSql = 
				  "UPDATE Customers "
				+ "SET Status = ? "
				+ "WHERE Id = ? ";

		String selectOrdersSql = 
				  "SELECT * "
				+ "FROM Orders "
				+ "WHERE CustomerId = ? ";

		try {
			try {
				conn.setAutoCommit(false);

				Database.printSessionInfo(conn);

				System.out.println(Thread.currentThread().getName() + " request X lock on Customers");

				updateCustomer = conn.prepareStatement(updateCustomerOrderStatusSql);
				updateCustomer.setString(1, orderStatus);
				updateCustomer.setInt(2, customerId);
				updateCustomer.executeUpdate();

				System.out.println(Thread.currentThread().getName() + " request S lock on Customers");

				selectOrders = conn.prepareStatement(selectOrdersSql);
				selectOrders.setInt(1, customerId);
				ResultSet rows = selectOrders.executeQuery();

				while (rows.next()) {
					orders.add(new Order(rows.getInt(1), // ORDER_ID
							rows.getInt(2), // CUSTOMER_ID
							rows.getDate(3), // DATE
							rows.getFloat(4), // TOTAL
							rows.getString(5) // STATUS
					));
				}
				conn.commit();

			} catch (SQLException ex) {

				conn.rollback();
				throw ex;

			} finally {
				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orders;
	}

	public Customer addOrder(int customerId, float orderTotal) {

		Connection conn = Database.getConnection(Connection.TRANSACTION_SERIALIZABLE);

		Customer customer = null;

		PreparedStatement insertOrder = null;
		PreparedStatement selectCustomer = null;
		PreparedStatement updateCustomer = null;

		String insertOrderSql = 
				"INSERT INTO Orders (CustomerId, Date, Total, Status) "
				+ "VALUES (?, GETDATE(), ?, 'A')";

		String selectCustomerSql = 
				"SELECT Id, Name, 'A' as Status "
				+ "FROM Customers "
				+ "WHERE Id = ?";

		String updateCustomerSql = 
				"UPDATE Customers "
				+ "SET Status = 'A' "
				+ "WHERE Id = ? ";

		try {
			try {
				conn.setAutoCommit(false);

				Database.printSessionInfo(conn);

				System.out.println(Thread.currentThread().getName() + " request X lock on ORDERS");

				insertOrder = conn.prepareStatement(insertOrderSql);
				insertOrder.setInt(1, customerId);
				insertOrder.setFloat(2, orderTotal);
				insertOrder.executeUpdate();

				System.out.println(Thread.currentThread().getName() + " request S lock on CUSTOMERS");

				selectCustomer = conn.prepareStatement(selectCustomerSql);
				selectCustomer.setInt(1, customerId);
				ResultSet rows = selectCustomer.executeQuery();

				if (rows.next()) {
					customer = new Customer(rows.getInt(1), rows.getString(2), rows.getString(3));
				}

				System.out.println(Thread.currentThread().getName() + " request X lock on CUSTOMERS");

				updateCustomer = conn.prepareStatement(updateCustomerSql);
				updateCustomer.setInt(1, customerId);
				updateCustomer.executeUpdate();

				conn.commit();

			} catch (SQLException ex) {

				conn.rollback();
				throw ex;

			} finally {

				conn.setAutoCommit(true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return customer;
	}
}
