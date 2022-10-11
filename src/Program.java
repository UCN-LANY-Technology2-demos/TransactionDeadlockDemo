import java.util.ArrayList;

import database.DataContext;
import model.Customer;
import model.Order;

public class Program {

	public static void main(String[] args) {

		Thread transaction1 = new Thread(new deadlockDemo.T1(), "T1");
		Thread transaction2 = new Thread(new deadlockDemo.T2(), "T2");

		transaction1.start();
		transaction2.start();

		try {
			
			transaction1.join();
			transaction2.join();
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}

		DataContext.resetDatabase();
	}
}
