package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

public class Payment {
	// A common method to connect to the DB
			private Connection connect() {
				Connection con = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");

					// Provide the correct details: DBServer/DBName, username, password
					con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/payment", "root", "");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return con;
			}
			
	public String insertPayment(int orderid, float amount) {
				String output = "";
				try {
					Connection con = connect();
					if (con == null) {
						return "Error while connecting to the database for inserting.";
					}
					// create a prepared statement
					String query = " insert into payment(`payID`,`orderID`,`totalAmount`)"+ "values (?, ?, ?)";
					PreparedStatement preparedStmt = con.prepareStatement(query);
					// binding values
					preparedStmt.setInt(1, 0);
					preparedStmt.setInt(2, orderid);
					preparedStmt.setFloat(3, amount);
					
					// execute the statement
					preparedStmt.execute();
					con.close();

					String newPayment = readPayments(); 
					 output = "{\"status\":\"success\", \"data\": \"" + 
							 newPayment + "\"}";
				} catch (Exception e) {
					output = "{\"status\":\"error\", \"data\": \"Error while inserting the item.\"}"; 
					System.err.println(e.getMessage()); 
				}
				return output;
			}

	public String readPayments() {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for reading.";
			}
			// Prepare the html table to be displayed
			output = "<table border='1'><tr><th>OrderID</th> <th>Amount</th> <th>Update</th> <th>Remove</th></tr>";

			String query = "select * from payment";
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(query);

			// iterate through the rows in the result set
			while (rs.next()) {
				String payID = Integer.toString(rs.getInt("payID"));
				String orderID = Integer.toString(rs.getInt("orderID"));
				String amount = Float.toString(rs.getFloat("totalAmount"));

				// Add into the html table
				output += "<tr><td><input id='hidPaymentIDUpdate' name='hidPaymentIDUpdate' type='hidden' value='" + payID
						 + "'>" + orderID + "</td>"; 
				output += "<td>" + amount + "</td>";
		

				// buttons
				output += "<td><input name='btnUpdate' type='button' value='Update' "
				+ "class='btnUpdate btn btn-secondary' data-payid='" + payID + "'></td>"
				+ "<td><input name='btnRemove' type='button' value='Remove' "
				+ "class='btnRemove btn btn-danger' data-payid='" + payID + "'></td></tr>";
			}
			con.close();

			// Complete the html table
			output += "</table>";
		} catch (Exception e) {
			output = "Error while reading the payment.";
			System.err.println(e.getMessage());
		}
		return output;
	}

	public String updatePayment(String pID, String oID, String total) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for updating.";
			}
			// create a prepared statement
			String query = "UPDATE payment SET orderID=?,totalAmount=? WHERE payID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			
			preparedStmt.setInt(1, Integer.parseInt(oID));
			preparedStmt.setFloat(2,Float.parseFloat(total));
			preparedStmt.setInt(3, Integer.parseInt(pID));
			// execute the statement
			preparedStmt.execute();
			con.close();

			String newPayment = readPayments(); 
			 output = "{\"status\":\"success\", \"data\": \"" + 
					 newPayment + "\"}";
			 
		} catch (Exception e) {
			 output = "{\"status\":\"error\", \"data\": \"Error while updating the item.\"}"; 
			 System.err.println(e.getMessage()); 
			}
		return output;
		}

	public String deletePayment(String payID) {
		String output = "";
		try {
			Connection con = connect();
			if (con == null) {
				return "Error while connecting to the database for deleting.";
			}
			// create a prepared statement
			String query = "delete from payment where payID=?";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			// binding values
			preparedStmt.setInt(1, Integer.parseInt(payID));
			// execute the statement
			preparedStmt.execute();
			con.close();

			String newPayment = readPayments(); 
			 output = "{\"status\":\"success\", \"data\": \"" + 
			 newPayment + "\"}"; 
		} catch (Exception e) {
			output = "{\"status\":\"error\", \"data\": \"Error while deleting the item.\"}"; 
			 System.err.println(e.getMessage());
		}
		return output;
	}
}
