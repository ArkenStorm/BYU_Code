package result;

/**
 * The type Message result.
 */
public class MessageResult extends Result {
	/**
	 * Either a success message or an error message
	 */
	private String message;

	/**
	 * Instantiates a new Message result.
	 *
	 * @param message the message
	 */
	public MessageResult(String message) {
		this.message = message;
	}

	/**
	 * Gets message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Sets message.
	 *
	 * @param message the message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
