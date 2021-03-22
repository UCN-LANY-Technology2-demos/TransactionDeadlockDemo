package model;

public class Customer {
	private int customerId;
	private String name;
	private String latestOrderStatus;

	public Customer(int customerId, String name, String latestOrderStatus) {
		super();
		this.customerId = customerId;
		this.name = name;
		this.latestOrderStatus = latestOrderStatus;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLatestOrderStatus() {
		return latestOrderStatus;
	}

	public void setLatestOrderStatus(String latestOrderStatus) {
		this.latestOrderStatus = latestOrderStatus;
	}

	public int getCustomerId() {
		return customerId;
	}

	@Override
	public String toString() {
		return "Customer: " + name + ", Latest Order Status: " + latestOrderStatus;
	}

}
