
import java.io.Serializable;

public class User implements Serializable, Comparable<User>
{
	private static final long serialVersionUID = -7020619477594468968L;
	public String nick;
	public String pass;
	public boolean isNewUser;

	User(String nick, String pass)
	{
		this.nick = nick;
		this.pass = pass;
		this.isNewUser = false;
	}

	public int compareTo(User other)
	{
		if (this.pass.hashCode() == other.pass.hashCode()
				&& this.nick.hashCode() == other.nick.hashCode())
			return 1;
		else
			return 0;
	}
}