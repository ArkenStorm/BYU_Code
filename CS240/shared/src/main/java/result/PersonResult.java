package result;

import model.Person;

/**
 * A Person result.
 */
public class PersonResult extends Result {
	/**
	 * The User (username) associated with the Person
	 */
	private String associatedUsername;

	/**
	 * The ID of the Person
	 */
	private String personID;

	/**
	 * The first name of the Person
	 */
	private String firstName;

	/**
	 * The last name of the Person
	 */
	private String lastName;

	/**
	 * The gender of the Person
	 */
	private String gender;

	/**
	 * Instantiates a new Person result.
	 *
	 * @param person a person object.
	 */
	public PersonResult(Person person) {
		this.associatedUsername = person.getUsername();
		this.personID = person.getPersonID();
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.gender = person.getGender();
	}

	/**
	 * Gets associated username.
	 *
	 * @return the associated username
	 */
	public String getAssociatedUsername() {
		return associatedUsername;
	}

	/**
	 * Sets associated username.
	 *
	 * @param associatedUsername the associated username
	 */
	public void setAssociatedUsername(String associatedUsername) {
		this.associatedUsername = associatedUsername;
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
