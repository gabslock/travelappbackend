/*
@name: Gabriel Jucá
@date: 21st Nov 2021
Expense.java
 */
package ie.cct.travelappbackend.model;

//Expense class that will be the model of every expense object

public class Expense {

	// Every expense has amount, description, trip and user as variables
	private double amount;
	private String description;
	private String trip;
	private String user;

	public Expense() {
	}

	public Expense(double amount, String description, String trip, String user) {
		super();
		this.amount = amount;
		this.description = description;
		this.trip = trip;
		this.user = user;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTrip() {
		return trip;
	}

	public void setTrip(String trip) {
		this.trip = trip;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
