package model;


import java.util.Objects;

/**
 * An Event in some Person's life.
 */
public class Event {
	/**
	 * The ID of the Event.
	 */
	private String eventID;

	/**
	 * The User (username) that the Event is associated with.
	 */
	private String associatedUsername;

	/**
	 * The ID of the associated Person.
	 */
	private String personID;

	/**
	 * The latitude of the Event's location
	 */
	private Double latitude;

	/**
	 * The longitude of the Event's location
	 */
	private Double longitude;

	/**
	 * The country in which the Event occurred
	 */
	private String country;

	/**
	 * The city in which the Event occurred
	 */
	private String city;

	/**
	 * The type of Event (birth, baptism, christening, marriage, death, etc.)
	 */
	private String eventType;

	/**
	 * The year the Event occurred
	 */
	private int year;

	/**
	 * Instantiates a new Event.
	 *
	 * @param eventID   the event id
	 * @param username  the username
	 * @param personID  the person id
	 * @param latitude  the latitude
	 * @param longitude the longitude
	 * @param country   the country
	 * @param city      the city
	 * @param eventType the event type
	 * @param year      the year
	 */
	public Event(String eventID, String username, String personID, Double latitude, Double longitude, String country, String city, String eventType, int year) {
		this.eventID = eventID;
		this.associatedUsername = username;
		this.personID = personID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.country = country;
		this.city = city;
		this.eventType = eventType;
		this.year = year;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Event event = (Event) o;
		return year == event.year &&
				eventID.equals(event.eventID) &&
				associatedUsername.equals(event.associatedUsername) &&
				personID.equals(event.personID) &&
				latitude.equals(event.latitude) &&
				longitude.equals(event.longitude) &&
				country.equals(event.country) &&
				city.equals(event.city) &&
				eventType.equals(event.eventType);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year);
	}

	/**
	 * Gets event id.
	 *
	 * @return the event id
	 */
	public String getEventID() {
		return eventID;
	}

	/**
	 * Sets event id.
	 *
	 * @param eventID the event id
	 */
	public void setEventID(String eventID) {
		this.eventID = eventID;
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
	 * Gets latitude.
	 *
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * Sets latitude.
	 *
	 * @param latitude the latitude
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}


	/**
	 * Gets longitude.
	 *
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * Sets longitude.
	 *
	 * @param longitude the longitude
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}


	/**
	 * Gets country.
	 *
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets country.
	 *
	 * @param country the country
	 */
	public void setCountry(String country) {
		this.country = country;
	}


	/**
	 * Gets city.
	 *
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Sets city.
	 *
	 * @param city the city
	 */
	public void setCity(String city) {
		this.city = city;
	}


	/**
	 * Gets event type.
	 *
	 * @return the event type
	 */
	public String getEventType() {
		return eventType;
	}

	/**
	 * Sets event type.
	 *
	 * @param eventType the event type
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}


	/**
	 * Gets year.
	 *
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * Sets year.
	 *
	 * @param year the year
	 */
	public void setYear(int year) {
		this.year = year;
	}

}
