package com.core.java.sqlconnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class AtmFunctions {
	private static final int NULL = 0;
	static Connection con=DataBaseConnectivity.getConnection();
	Scanner sc=new Scanner(System.in);
	String sql="";
	static PreparedStatement pst;
	static ResultSet rs;
	int amount;
	int balance;
	public void loginValidation(int accno, int password) {
		try {
			if(accno==NULL || password==NULL) {
				System.out.println("Enter Valid Details!!!");
			}
			sql="SELECT * from LoginDetails WHERE accno="+accno+" and password="+password;
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
			if(rs.next()) {
				System.out.println("HI!!!,"+rs.getInt(1));
				System.out.println("****************************************************************************************Welcome To XYZ Bank****************************************************************************************");
				while(true) {
					System.out.println("1.Check Balance");
					System.out.println("2.Deposit");
					System.out.println("3.Withdraw");
					System.out.println("4.Transact");
					System.out.println("5.Transaction History");
					System.out.println("6.Exit");
					System.out.println("Enter The Choice=");
					int choice=sc.nextInt();
					switch(choice) {
					case 1:
						checkBalance(accno);
						System.out.println("********************************************************************************************************************************************************************************");
						break;
					case 2:
						depositAmount(accno);
						System.out.println("********************************************************************************************************************************************************************************");
						break;
					case 3:
						withdrawAmount(accno);
						System.out.println("********************************************************************************************************************************************************************************");
						break;
					case 4:
						transactAmount(accno);
						System.out.println("********************************************************************************************************************************************************************************");
						break;
						case 5:
						transactionHistory(accno);
						System.out.println("********************************************************************************************************************************************************************************");
						break;
					case 6:
						System.out.println("Thanks For Visiting!!!");
						System.exit(0);
						break;
					}
			   }
			}
			else {
				System.out.println("Invalid User Details!!!");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public int transactAmount(int accno) {
		int raccno;
		try {
			sql="SELECT * from UserDetails where accno="+accno;
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
			if(rs.next()) {
				System.out.println("Enter The Receiver's Account Number=");
				raccno=sc.nextInt();
				if(raccno==NULL) {
					System.out.println("Account Number Can't Be Null");
				}
				else {
				sql="SELECT * from UserDetails WHERE accno="+raccno;
				pst=con.prepareStatement(sql);
				rs=pst.executeQuery();
				if(rs.next()) {
					System.out.println("Enter The Amount=");
					amount=sc.nextInt();
					if(amount==NULL || amount<0)
					{
						System.out.println("Invalid Amount!!!");
					}
					else {
					Statement st=con.createStatement();
					sql="SELECT * from UserDetails WHERE accno="+accno;
					pst=con.prepareStatement(sql);
					rs=pst.executeQuery();
					while(rs.next()) {
					sql="INSERT into TransactionDetails(accno,amount,status,date,balance) values("+accno+","+amount+",'txn-to-"+raccno+"',current_timestamp,"+(rs.getInt("balance")-amount)+")";
					if(st.executeUpdate(sql)==1)
	                {
	                	System.out.println("Money Has Been Sent.");
	                }
					else {
	                	System.out.println("Somthing Went Wrong!!!Try Again.");
	                }
					sql="UPDATE UserDetails SET balance=balance-"+amount+" WHERE accno="+accno;
					st.executeUpdate(sql);
					}
					
	                sql="SELECT * from UserDetails WHERE accno="+raccno;
	                pst=con.prepareStatement(sql);
	                rs=pst.executeQuery();
	                while(rs.next()) {
	                sql="INSERT into TransactionDetails(accno,amount,status,date,balance) values("+raccno+","+amount+",'txn-from-"+accno+"',current_timestamp,"+(rs.getInt("balance")+amount)+")";
	                st.executeUpdate(sql);
	                }
	                sql="UPDATE UserDetails SET balance=balance+"+amount+" WHERE accno="+raccno;
	                st.executeUpdate(sql);
	                
				}}else {
					System.out.println("No Such Account Number!!! Please Enter Correct Account Number.");
				}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return accno;
		
		
	}
	public int transactionHistory(int accno) {
		try {
			sql="SELECT u.accno,u.holder,t.amount,t.status,t.date,t.balance from UserDetails u,TransactionDetails t WHERE u.accno="+accno+" and t.accno="+accno;
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
				System.out.println("ACC_NO\tACC_HOLDER\tAMOUNT\tSTATUS\tDATE\t\t\tBALANCE");
				while(rs.next()) {
					System.out.println(accno+"\t"+rs.getString("holder")+"\t\t"+rs.getInt("amount")+"\t"+rs.getString("status")+"\t"+rs.getTimestamp("date")+"\t"+rs.getInt("balance"));
					}
				}
		catch(Exception e) {
			e.printStackTrace();
		}
		return accno;
	}
	public int withdrawAmount(int accno) {
		try {
			sql="SELECT * from UserDetails WHERE accno="+accno;
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
			if(rs.next()) {
				System.out.println("Enter The Amount=");
				amount=sc.nextInt();
				if(amount==NULL || amount<0 || (amount>rs.getInt("balance"))) {
					System.out.println("Not Valid Or Insufficient Fund!!!");
				}
				else {
					Statement st=con.createStatement();
					sql="UPDATE UserDetails SET balance=balance-"+amount+" WHERE accno="+accno;
					st.executeUpdate(sql);
					sql="INSERT into TransactionDetails(accno,amount,status,date,balance) values("+accno+","+amount+",'with',current_timestamp,"+(rs.getInt("balance")-amount)+")";
					if(st.executeUpdate(sql)==1) {
						System.out.println("Money Has Been Withdrawn Successfully.");
					}
					else {
						System.out.println("Something Went Wrong!!!Try Again.");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			}
		return balance;	
	}
	public int depositAmount(int accno) {
		try {
			sql="SELECT * from UserDetails WHERE accno="+accno;
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
			if(rs.next()) {
				System.out.println("Enter The Amount=");
				amount=sc.nextInt();
				if(amount==NULL || amount<0) {
					System.out.println("Not A Valid Amount!!!");
				}
				else {
					Statement st=con.createStatement();
					sql="UPDATE UserDetails SET balance=balance+"+amount+" WHERE accno="+accno;
	                st.executeUpdate(sql);
					// pst.executeUpdate(sql);	                
					sql="INSERT into TransactionDetails(accno,amount,status,date,balance) values("+accno+","+amount+",'dep',current_timestamp,"+(rs.getInt("balance")+amount)+")";
					if(st.executeUpdate(sql)==1) {
						System.out.println("Money Has Been Deposited Successfully.");
					}
					else {
						System.out.println("Something Went Wrong!!!Try Again.");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			}
		return balance;
		
		
	}
	public int checkBalance(int accno) {
		try {
			sql="SELECT * from UserDetails WHERE accno="+accno;
			pst=con.prepareStatement(sql);
			rs=pst.executeQuery();
			System.out.println("ACCOUNT_NUMBER"+"\t"+"HOLDER"+"\t"+"BALANCE");
			if(rs.next()) {
				System.out.println(rs.getInt("accno")+"\t\t"+rs.getString("holder")+"\t"+rs.getInt("balance"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return balance;
		
		
	}
		

}
