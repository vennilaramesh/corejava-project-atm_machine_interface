package com.core.java.sqlconnectivity;

import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		int attempt = 3;
		if(attempt<=3) {
		for(int i=0;i<3;i++) {
	    			
		System.out.println("Enter Account Number=");
		int accno=sc.nextInt();
		System.out.println("Enter Password=");
		int password=sc.nextInt();
		AtmFunctions atm=new AtmFunctions();
		atm.loginValidation(accno,password);
		attempt--;
		System.out.println("You Have  "+attempt+" More Attempts To Access");
		}
			
		}
  System.out.println("Your Account Is Blocked!!!Contact Your Nearest Branch.");
	}

}
