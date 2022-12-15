package deadlockDemo;

import java.sql.Connection;

public abstract class TransactionBase {

	protected int isolationLevel = Connection.TRANSACTION_READ_UNCOMMITTED;

	public TransactionBase (int isolationLevel) {
		
		this.isolationLevel = isolationLevel;
	}
}
