package ClientCore;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

import Object.User;

public class Client {
	public static Boolean isLoggedIn = false;
	public static Boolean isConnected = false;
	public static User user;
	public static ArrayList<User> friends = new ArrayList<User>();
	private static Scanner sc; 
	public static void main(String [] args){
		sc = new Scanner(System.in);
		int loginNumber;
		System.out.println("Welcome. Choose 0 for sign up. 1 for login");
		loginNumber = sc.nextInt();
		String nick;
		String password;
		if(loginNumber == 0)
			System.out.println("Welcome to User Sign Up Interface");
		else
			System.out.println("Welcome to User Login Interface");
		System.out.println("Enter your Nick: ");
		nick = sc.next();
		System.out.println("Enter your password: ");
		password = sc.next();
		user = new User(nick, password);
		if(loginNumber == 0)
			user.isNewUser = true;
		else
			user.isNewUser = false;
		(new LoginRequestThread(user)).start();
		//getIP();
		
	}
	public static void getIP(){
		try
        {
            Enumeration<?> e = NetworkInterface.getNetworkInterfaces();
            while(e.hasMoreElements())
            {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration<?> ee = n.getInetAddresses();
                int count = 0;
                while (ee.hasMoreElements())
                {
                	count++;
                    InetAddress i = (InetAddress) ee.nextElement();
                    String address; 
                    address = i.getHostAddress(); 
                    System.out.println(count +" : " + address);
                }
            } 
        }
        catch( SocketException SE)
        {
            SE.printStackTrace();
        }
	}
}
