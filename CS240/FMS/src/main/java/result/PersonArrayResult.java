package result;

import model.Person;

import java.util.List;

/**
 * A list of Persons
 */
public class PersonArrayResult extends Result {
	/**
	 * The list of Persons
	 */
	private List<Person> data;

	/**
	 * Instantiates a new Person array result.
	 *
	 * @param data the data
	 */
	public PersonArrayResult(List<Person> data) {
		this.data = data;
	}

	/**
	 * Gets data.
	 *
	 * @return the data
	 */
	public List<Person> getData() {
		return data;
	}

	/**
	 * Sets data.
	 *
	 * @param data the data
	 */
	public void setData(List<Person> data) {
		this.data = data;
	}
}
