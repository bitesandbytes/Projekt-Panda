import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	public String message;

	Message(String msg) {
		message = msg;
	}
}
