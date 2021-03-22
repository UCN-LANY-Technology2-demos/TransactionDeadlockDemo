package model;

import java.util.Date;

public class Order {

	private int orderId;
	private int customerId;
	private Date date;
	private float total;
	private String status;

	public Order(int orderId, int customerId, Date date, float total, String status) {
		super();
		this.orderId = orderId;
		this.customerId = customerId;
		this.date = date;
		this.total = total;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getOrderId() {
		return orderId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public Date getDate() {
		return date;
	}

	public float getTotal() {
		return total;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append("Order: " + orderId + ", ");
		sb.append("Customer: " + customerId + ", ");
		sb.append("Date: " + date + "; ");
		sb.append("Total; " + total);

		return sb.toString();
	}

}
