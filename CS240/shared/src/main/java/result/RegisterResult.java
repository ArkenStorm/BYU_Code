package result;
// Note: this will never be used, as AuthorizationResult actually takes care of everything necesaary for login/request results


/**
 * The result of a registration request.
 */
public class RegisterResult extends Result {
	/**
	 * The username given to the User
	 */
	private String userName;

	/**
	 * The User's password
	 */
	private String password;

	/**
	 * The User's email
	 */
	private String email;

	/**
	 * The User's first name
	 */
	private String firstName;

	/**
	 * The User's last name
	 */
	private String lastName;

	/**
	 * the User's gender
	 */
	private String gender;

	/**
	 * Instantiates a new Register result.
	 *
	 * @param userName  the user name
	 * @param password  the password
	 * @param email     the email
	 * @param firstName the first name
	 * @param lastName  the last name
	 * @param gender    the gender
	 */
	public RegisterResult(String userName, String password, String email, String firstName, String lastName, String gender) {
		this.userName = userName;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
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

	/**
	 * Gets email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 *
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets first name.
	 *
	 * @return the first name
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Sets first name.
	 *
	 * @param firstName the first name
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets last name.
	 *
	 * @return the last name
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Sets last name.
	 *
	 * @param lastName the last name
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets gender.
	 *
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * Sets gender.
	 *
	 * @param gender the gender
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
}
