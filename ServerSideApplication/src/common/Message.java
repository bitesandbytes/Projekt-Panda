package common;

import java.io.Serializable;

public class Message implements Serializable
{
	private static final long serialVersionUID = -7020619477594468966L;
	public String sourceNick;
	public String destNick;
	public String content;

	Message(String sn, String dn, String c)
	{
		sourceNick = sn;
		destNick = sn;
		content = c;
	}

}