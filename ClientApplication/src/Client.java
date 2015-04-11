import java.util.Scanner;

public class Client {
	public static Boolean isLoggedIn = false;
	public static Boolean isConnected = false;
	public static PostLoginRequestThread afterLogin;
	public static User user;
	public static void main(String [] args){
		Scanner sc = new Scanner(System.in);
		int n;
		System.out.println("Welcome. Choose 0 for sign up. 1 for login");
		n = sc.nextInt();
		String nick;
		String password;
		if(n == 0)
			System.out.println("Welcome to User Sign Up Interface");
		else
			System.out.println("Welcome to User Login Interface");
		System.out.println("Enter your Nick: ");
		nick = sc.next();
		System.out.println("Enter your password: ");
		password = sc.next();
		user = new User(nick, password);
		if(n == 0)
			user.isNewUser = true;
		else
			user.isNewUser = false;
		(new LoginRequestThread(user)).start();
		
	}
}
