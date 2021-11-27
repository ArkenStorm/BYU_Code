package model;


import java.util.Objects;

/**
 * A Person that either once lived or is living.
 */
public class Person {
	/**
	 * The ID of the Person
	 */
	private String personID;

	/**
	 * The User (username) associated with the Person
	 */
	private String associatedUsername;

	/**
	 * The Person's first name
	 */
	private String firstName;

	/**
	 * The Person's last name
	 */
	private String lastName;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return personID.equals(person.personID) &&
				associatedUsername.equals(person.associatedUsername) &&
				firstName.equals(person.firstName) &&
				lastName.equals(person.lastName) &&
				gender.equals(person.gender) &&
				Objects.equals(fatherID, person.fatherID) &&
				Objects.equals(motherID, person.motherID) &&
				Objects.equals(spouseID, person.spouseID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID);
	}

	/**
	 * The gender (m or f) of the Person
	 */
	private String gender;

	/**
	 * The ID of the Person's father, if it exists
	 */
	private String fatherID;

	/**
	 * The ID of the Person's mother, if it exists
	 */
	private String motherID;

	/**
	 * The ID of the Person's spouse, if it exists
	 */
	private String spouseID;

	/**
	 * Instantiates a new Person.
	 *
	 * @param personID  the person id
	 * @param username  the username
	 * @param firstName the first name
	 * @param lastName  the last name
	 * @param gender    the gender
	 * @param fatherID  the father id
	 * @param motherID  the mother id
	 * @param spouseID  the spouse id
	 */
	public Person(String personID, String username, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
		this.personID = personID;
		this.associatedUsername = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.fatherID = fatherID;
		this.motherID = motherID;
		this.spouseID = spouseID;
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
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return associatedUsername;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.associatedUsername = username;
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
	 * Gets father id.
	 *
	 * @return the father id
	 */
	public String getFatherID() {
		return fatherID;
	}

	/**
	 * Sets father id.
	 *
	 * @param fatherID the father id
	 */
	public void setFatherID(String fatherID) {
		this.fatherID = fatherID;
	}


	/**
	 * Gets mother id.
	 *
	 * @return the mother id
	 */
	public String getMotherID() {
		return motherID;
	}

	/**
	 * Sets mother id.
	 *
	 * @param motherID the mother id
	 */
	public void setMotherID(String motherID) {
		this.motherID = motherID;
	}


	/**
	 * Gets spouse id.
	 *
	 * @return the spouse id
	 */
	public String getSpouseID() {
		return spouseID;
	}

	/**
	 * Sets spouse id.
	 *
	 * @param spouseID the spouse id
	 */
	public void setSpouseID(String spouseID) {
		this.spouseID = spouseID;
	}

}
