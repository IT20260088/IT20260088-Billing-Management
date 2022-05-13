package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


public class Billing {
	
	private Connection connect() {
		Connection con = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Provide the correct details: DBServer/DBName, username, password
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/billsystem?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return con;
	}

	public String insertBilling(String AccountNo, String BillingStartDate, String BillingEndDate, String NoOfUnits, String ArrearsAmount)  
	{   
		String output = ""; 	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for inserting."; } 
	 
			// create a prepared statement 
			String query = " insert into billings(`Bill_ID`,`AccountNo`,`BillingStartDate`,`BillingEndDate`,`NoOfUnits`,`ArrearsAmount`)" + " values (?, ?, ?, ?, ?, ?)"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			 preparedStmt.setInt(1, 0);
			 preparedStmt.setString(2, AccountNo);
			 preparedStmt.setString(3, BillingStartDate);
			 preparedStmt.setString(4, BillingEndDate);
			 preparedStmt.setString(5, NoOfUnits);
			 preparedStmt.setString(6, ArrearsAmount);
			
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	   
			String newBilling = readBilling(); 
			output =  "{\"status\":\"success\", \"data\": \"" + newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while inserting the billing.\"}";  
			System.err.println(e.getMessage());   
		} 		
	  return output;  
	} 	
	
	public String readBilling()  
	{   
		String output = ""; 
		try   
		{    
			Connection con = connect(); 
		
			if (con == null)    
			{
				return "Error while connecting to the database for reading."; 
			} 
	 
			// Prepare the html table to be displayed    
			output = "<table border=\'1\'><tr><th>Account No</th><th>Start Date</th><th>End Date</th><th>No Of Units</th><th>Amount</th><th>Update</th><th>Remove</th></tr>";
	 
			String query = "select * from billings";    
			Statement stmt = (Statement) con.createStatement();
			ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);
	 
			// iterate through the rows in the result set    
			while (rs.next())    
			{     
				 String Bill_ID = Integer.toString(rs.getInt("Bill_ID"));
				 String AccountNo = rs.getString("AccountNo");
				 String BillingStartDate = rs.getString("BillingStartDate");
				 String BillingEndDate = rs.getString("BillingEndDate");
				 String NoOfUnits = rs.getString("NoOfUnits");
				 String ArrearsAmount = rs.getString("ArrearsAmount");
				 
				// Add into the html table 
				output += "<tr><td><input id=\'hidBillingIDUpdate\' name=\'hidBillingIDUpdate\' type=\'hidden\' value=\'" + Bill_ID + "'>" 
							+ AccountNo + "</td>"; 
				output += "<td>" + BillingStartDate + "</td>";
				output += "<td>" + BillingEndDate + "</td>";
				output += "<td>" + NoOfUnits + "</td>";
				output += "<td>" + ArrearsAmount + "</td>";
	 
				// buttons     
				output +="<td><input name='btnUpdate' type='button' value='Update' class='btnUpdate btn btn-secondary'></td>"       
						+ "<td><input name='btnRemove' type='button' value='Remove' class='btnRemove btn btn-danger' data-billingid='" + Bill_ID + "'>" + "</td></tr>"; 
			
			}
			con.close(); 
	   
			output += "</table>";   
		}   
		catch (Exception e)   
		{    
			output = "Error while reading the billing.";    
			System.err.println(e.getMessage());   
		} 	 
		return output;  
	}
	
	public String updateBilling(String Bill_ID, String AccountNo, String BillingStartDate, String BillingEndDate, String NoOfUnits, String ArrearsAmount)  
	{   
		String output = "";  
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{return "Error while connecting to the database for updating."; } 
	 
			// create a prepared statement    
			String query = "UPDATE billings SET AccountNo=?,BillingStartDate=?,BillingEndDate=?,NoOfUnits=?,ArrearsAmount=?"  + "WHERE Bill_ID=?";  	 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setString(1, AccountNo);
			 preparedStmt.setString(2, BillingStartDate);
			 preparedStmt.setString(3, BillingEndDate);
			 preparedStmt.setString(4, NoOfUnits);
			 preparedStmt.setString(5, ArrearsAmount);
			 preparedStmt.setInt(6, Integer.parseInt(Bill_ID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close();  
			String newBilling = readBilling();    
			output = "{\"status\":\"success\", \"data\": \"" + newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output =  "{\"status\":\"error\", \"data\": \"Error while updating the billing.\"}";   
			System.err.println(e.getMessage());   
		} 	 
	  return output;  
	} 
	
	public String deleteBilling(String Bill_ID)   
	{   
		String output = ""; 
	 
		try   
		{    
			Connection con = connect(); 
	 
			if (con == null)    
			{
				return "Error while connecting to the database for deleting."; 			
			} 
	 
			// create a prepared statement    
			String query = "delete from billings where Bill_ID=?"; 
			PreparedStatement preparedStmt = con.prepareStatement(query); 
	 
			// binding values    
			preparedStmt.setInt(1, Integer.parseInt(Bill_ID)); 
	 
			// execute the statement    
			preparedStmt.execute();    
			con.close(); 
	 
			String newBilling = readBilling();    
			output = "{\"status\":\"success\", \"data\": \"" +  newBilling + "\"}";    
		}   
		catch (Exception e)   
		{    
			output = "Error while deleting the billing.";    
			System.err.println(e.getMessage());   
		} 
	 
		return output;  
	}
	
}
