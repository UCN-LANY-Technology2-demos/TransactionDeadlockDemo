import java.sql.Connection;

import database.DataContext;

public class Program {

	public static void main(String[] args) {

		Thread transaction1 = new Thread(new deadlockDemo.Transaction1(Connection.TRANSACTION_READ_UNCOMMITTED), "T1");
		Thread transaction2 = new Thread(new deadlockDemo.Transaction2(Connection.TRANSACTION_READ_UNCOMMITTED), "T2");

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
