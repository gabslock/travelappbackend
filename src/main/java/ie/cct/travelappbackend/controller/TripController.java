/*
@name: Gabriel Jucá
@date: 21st Nov 2021
TravelappbackendApplication.java

Back-end of a Travel App application that allows the user to share expenses of a trip with their friends.
This code was done as an integrated assignment for the Higher Diploma in Science in Computing (CCT Dublin).

 */
package ie.cct.travelappbackend.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ie.cct.travelappbackend.model.User;
import ie.cct.travelappbackend.model.Expense;

//Controller class with HTTP requests (GET and POST) and all the methods needed for the application.

@RestController
@CrossOrigin(origins = "*")
public class TripController {

	// Variables ArrayLists of users and expenses
	private List<User> users;
	private List<Expense> listOfExpenses;

	// Constructor that will initialize users and expenses based in the text files
	public TripController() {

		// Reading text file to get an array list of users
		// https://owlcation.com/stem/Java-BufferedReader-and-BufferedWriter-Example
		users = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
			String line = "";
			while ((line = br.readLine()) != null) {
				String[] user = line.split(" ");
				users.add(new User(user[0], user[1]));
			}
			br.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// Reading text file to get an array list of expenses
		// https://owlcation.com/stem/Java-BufferedReader-and-BufferedWriter-Example
		listOfExpenses = new ArrayList<>();
		try (BufferedReader br2 = new BufferedReader(new FileReader("expenses.txt"))) {
			String line = "";
			while ((line = br2.readLine()) != null) {
				String[] expense = line.split(" ");
				listOfExpenses.add(new Expense(Double.parseDouble(expense[0]), expense[1], expense[2], expense[3]));
			}
			br2.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Method to reinitialize the array list of expenses when needed
	public void reinitialize() {
		listOfExpenses.clear();
		try (BufferedReader br2 = new BufferedReader(new FileReader("expenses.txt"))) {
			String line = "";
			while ((line = br2.readLine()) != null) {
				String[] expense = line.split(" ");
				listOfExpenses.add(new Expense(Double.parseDouble(expense[0]), expense[1], expense[2], expense[3]));
			}
			br2.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// Login method that compares the parameters requested with the usernames and
	// passwords of the text file
	@GetMapping("login")
	public boolean login(@RequestParam("username") String username, @RequestParam("password") String password) {

		for (User user : users) {
			if (user.getUsername().contentEquals(username) && user.getPassword().contentEquals(password)) {
				return true;
			}
		}
		return false;
	}

	// Posting an expense in the text file using a post method
	@RequestMapping(value = "/postexpense", method = RequestMethod.POST)
	@ResponseBody
	public void postExpense(String amount, String description, String trip, String user) throws Exception {

		String s = System.lineSeparator();
		Writer writer = new BufferedWriter(new FileWriter("expenses.txt", true));
		writer.write(s);
		writer.append(amount + " " + description + " " + trip + " " + user);
		writer.close();
	}

	// Getting the list of expenses of a trip
	@GetMapping("trip")
	public List<Expense> returnExpense(@RequestParam("tripName") String tripName) {

		reinitialize();

		// Creating a new array with with expenses with same name of the parameter
		// requested
		List<Expense> returnExpense = new ArrayList();

		for (int i = 0; i < listOfExpenses.size(); i++) {
			if (listOfExpenses.get(i).getTrip().equals(tripName)) {
				returnExpense.add(listOfExpenses.get(i));
			}
		}
		return returnExpense;
	}

	// Closing a trip and getting the final information according to the user
	@GetMapping("closetrip")
	public String[] tripReport(@RequestParam("tripName") String tripName, @RequestParam("username") String username) {

		reinitialize();

		// Variables that will be used to calculate the resume of the trip
		double totalAmount = 0; // Total of expenses of the trip
		int numberOfPurchases = 0; // Number of purchases of the trip
		double averageAmount; // Average amount of the trip
		String highestPurchaseName = null; // Description of the highest purchase of the trip
		double highestPurchaseAmount = 0; // Amount of the highest purchase of the trip
		String lowestPurchaseName = null; // Description of the lowest purchase of the trip
		double lowestPurchaseAmount = 0; // Amount of the lowest purchase of the trip
		double userAmount = 0; // How much an user spent
		double userToBePaid = 0; // How much an user needs to be paid

		// Calculating the total amount of the trip
		for (int i = 0; i < listOfExpenses.size(); i++)
			if (listOfExpenses.get(i).getTrip().equals(tripName)) {
				totalAmount = totalAmount + listOfExpenses.get(i).getAmount();
			}

		// Calculating the number of purchases
		for (int i = 0; i < listOfExpenses.size(); i++)
			if (listOfExpenses.get(i).getTrip().equals(tripName)) {
				numberOfPurchases = numberOfPurchases + 1;
			}

		// Calculating the average amount
		averageAmount = totalAmount / numberOfPurchases;

		// Finding highest purchase
		for (int i = 0; i < listOfExpenses.size(); i++)
			if (listOfExpenses.get(i).getTrip().equals(tripName)) {
				if (highestPurchaseAmount == 0) {
					highestPurchaseAmount = listOfExpenses.get(i).getAmount();
					highestPurchaseName = listOfExpenses.get(i).getDescription();
				} else if (listOfExpenses.get(i).getAmount() > highestPurchaseAmount) {
					highestPurchaseAmount = listOfExpenses.get(i).getAmount();
					highestPurchaseName = listOfExpenses.get(i).getDescription();
				}
			}

		// Finding lowest purchase
		for (int i = 0; i < listOfExpenses.size(); i++)
			if (listOfExpenses.get(i).getTrip().equals(tripName)) {
				if (lowestPurchaseAmount == 0) {
					lowestPurchaseAmount = listOfExpenses.get(i).getAmount();
					lowestPurchaseName = listOfExpenses.get(i).getDescription();
				} else if (listOfExpenses.get(i).getAmount() < lowestPurchaseAmount) {
					lowestPurchaseAmount = listOfExpenses.get(i).getAmount();
					lowestPurchaseName = listOfExpenses.get(i).getDescription();
				}
			}

		// Getting how much an user paid
		for (int i = 0; i < listOfExpenses.size(); i++)
			if (listOfExpenses.get(i).getTrip().equals(tripName) && listOfExpenses.get(i).getUser().equals(username)) {
				userAmount = userAmount + listOfExpenses.get(i).getAmount();
			}

		// Creating an array to check the users who participated in a trip
		ArrayList<String> usersInTrip = new ArrayList<String>();
		for (int i = 0; i < listOfExpenses.size(); i++) {
			if (listOfExpenses.get(i).getTrip().equals(tripName)
					&& !usersInTrip.contains(listOfExpenses.get(i).getUser())) {
				usersInTrip.add(listOfExpenses.get(i).getUser());
			}
		}

		// Getting how much an user needs to pay (or to be paid)
		userToBePaid = totalAmount / usersInTrip.size() - userAmount;

		// Showing the results in an array
		String[] result = new String[6];

		result[0] = "Total amount of the trip: €" + totalAmount;
		result[1] = "Number of purchases: " + numberOfPurchases;
		result[2] = "Avarage amount: €" + averageAmount;
		result[3] = "Highest expense was €" + highestPurchaseAmount + " (" + highestPurchaseName + ")";
		result[4] = "Lowest expense was €" + lowestPurchaseAmount + " (" + lowestPurchaseName + ")";

		// Check if user needs to pay or to be paid to give different messages
		if (userToBePaid <= 0) {
			userToBePaid = -userToBePaid; // Changing negative value to positive to output in a more user friendly way
			result[5] = "You paid €" + userAmount + " of the total and need to be paid €" + userToBePaid;
		} else {
			result[5] = "You paid €" + userAmount + " of the total and need to pay €" + Math.abs(userToBePaid);
		}

		return result;
	}

}
