package model;


import java.util.Objects;

/**
 * A User of the Family Map App.
 */
public class User {
	/**
	 * The User's username
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return userName.equals(user.userName) &&
				password.equals(user.password) &&
				email.equals(user.email) &&
				firstName.equals(user.firstName) &&
				lastName.equals(user.lastName) &&
				gender.equals(user.gender) &&
				personID.equals(user.personID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userName, password, email, firstName, lastName, gender, personID);
	}

	/**
	 * The User's gender
	 */
	private String gender;

	/**
	 * The Person associated with the User
	 */
	private String personID;

	/**
	 * Instantiates a new User.
	 *
	 * @param username  the username
	 * @param password  the password
	 * @param email     the email
	 * @param firstName the first name
	 * @param lastName  the last name
	 * @param gender    the gender
	 * @param personID  the person id
	 */
	public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
		this.userName = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.personID = personID;
	}

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return userName;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.userName = username;
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
