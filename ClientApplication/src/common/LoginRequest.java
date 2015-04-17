package common;

import java.io.Serializable;

public class LoginRequest implements Serializable, Comparable<LoginRequest>
{
	private static final long serialVersionUID = -7020619477594468968L;
	public String nick;
	public String pass;
	public boolean isSignup;

	public LoginRequest(String nick, String pass)
	{
		this.nick = nick;
		this.pass = pass;
		this.isSignup = false;
	}

	public int compareTo(LoginRequest other)
	{
		if (this.pass.hashCode() == other.pass.hashCode()
				&& this.nick.hashCode() == other.nick.hashCode())
			return 0;
		else
			return 1;
	}
}