package request;

import java.io.Serializable;

/**
 * A Login Request.
 */
public class LoginRequest implements Serializable {
	/**
	 * The username used in the login attempt
	 */
	private String userName;

	/**
	 * The password used in the login attempt
	 */
	private String password;

	/**
	 * Instantiates a new Login request.
	 *
	 * @param userName the user name
	 * @param password the password
	 */
	public LoginRequest(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * Gets user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets user name.
	 *
	 * @param userName the user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets password.
	 *
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
}
