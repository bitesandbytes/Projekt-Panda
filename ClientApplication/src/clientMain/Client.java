package clientMain;

import java.util.ArrayList;
import java.util.Scanner;

import common.LoginRequest;
import coreClientThreads.LoginRequestThread;

/*
 * Note that LoginRequest is Equivalent to a User Object. It is used throughout
 * to access current Client's Nick
 */
public class Client
{
	public static Boolean isLoggedIn = false;
	public static Boolean isConnected = false;
	public static LoginRequest user;
	public static ArrayList<LoginRequest> friends = new ArrayList<LoginRequest>();
	private static Scanner sc;

	public static void main(String[] args)
	{
		sc = new Scanner(System.in);
		int loginNumber;
		System.out.println("Welcome. Choose 0 for sign up. 1 for login");
		loginNumber = sc.nextInt();
		String nick;
		String password;
		if (loginNumber == 0)
			System.out.println("Welcome to User Sign Up Interface");
		else
			System.out.println("Welcome to User Login Interface");
		System.out.println("Enter your Nick: ");
		nick = sc.next();
		System.out.println("Enter your password: ");
		password = sc.next();
		user = new LoginRequest(nick, password);
		if (loginNumber == 0)
			user.isSignup = true;
		else
			user.isSignup = false;
		(new LoginRequestThread(user)).start();
	}
}
