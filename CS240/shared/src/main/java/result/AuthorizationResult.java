package result;

/**
 * The result of a login attempt.
 */
public class AuthorizationResult extends Result {
	/**
	 * The generated AuthToken for a User
	 */
	private String authToken;

	/**
	 * The User (username) associated with the login
	 */
	private String userName;

	/**
	 * The ID of the Person associated with the User
	 */
	private String personID;

	/**
	 * Instantiates a new Login result.
	 *
	 * @param authToken the auth token
	 * @param userName  the user name
	 * @param personID  the person id
	 */
	public AuthorizationResult(String authToken, String userName, String personID) {
		this.authToken = authToken;
		this.userName = userName;
		this.personID = personID;
	}

	/**
	 * Gets auth token.
	 *
	 * @return the auth token
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * Sets auth token.
	 *
	 * @param authToken the auth token
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
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
	 * Gets person id.
	 *
	 * @return the person id
	 */
	public String getPersonID() {
		return personID;
	}

	/**
	 * Sets person id.
	 *
	 * @param personID the person id
	 */
	public void setPersonID(String personID) {
		this.personID = personID;
	}
}
