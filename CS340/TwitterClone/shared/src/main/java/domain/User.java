package domain;

import java.util.Objects;

public class User implements Comparable<User> {

	private String firstName;
	private String lastName;
	private String alias;
	private String imageUrl;

	public User(String firstName, String lastName, String imageURL) {
		this(firstName, lastName, String.format("@%s%s", firstName, lastName), imageURL);
	}

	public User(String firstName, String lastName, String alias, String imageURL) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.alias = alias.charAt(0) == '@' ? alias : "@" + alias;
		this.imageUrl = imageURL;
	}

	public User() {}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getName() {
		return String.format("%s %s", firstName, lastName);
	}

	public String getAlias() {
		return alias;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return alias.equals(user.alias);
	}

	@Override
	public int hashCode() {
		return Objects.hash(alias);
	}


	@Override
	public String toString() {
		return "User{" +
				"firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", alias='" + alias + '\'' +
				", imageUrl='" + imageUrl + '\'' + // problem if no image url
				'}';
	}

	@Override
	public int compareTo(User user) {
		return this.getAlias().compareTo(user.getAlias());
	}
}
