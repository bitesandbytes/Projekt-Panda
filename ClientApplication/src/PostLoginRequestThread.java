
public class PostLoginRequestThread extends Thread{
	public PostLoginRequestThread() {
		super();
	}
	public void run(){
		System.out.println("Successful Login");
		System.out.println("Welcome : " +  Client.user.nick);
	}

}
