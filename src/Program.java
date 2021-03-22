import java.util.ArrayList;

import model.Customer;
import model.Order;

public class Program {

	public static void main(String[] args) {

		CustomerOrderDeadlock example = new CustomerOrderDeadlock();

		Thread t1 = new Thread("Transaction 1") {
			public void run() {

				ArrayList<Order> orders = example.updateCustomerOrderStatus(1, "A");

				for (Order order : orders) {

					System.out.println(order);
				}
			}
		};

		Thread t2 = new Thread("Transaction 2") {
			public void run() {

				Customer customer = example.addOrder(1, 100);

				System.out.println(customer);
			}
		};

		t1.start();
		t2.start();
	}
}
