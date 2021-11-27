package result;

import model.Event;

/**
 * An Event result
 */
public class EventResult extends Result {
	/**
	 * The User (username) that the Event is associated with
	 */
	private String associatedUsername;

	/**
	 * The ID of the Event
	 */
	private String eventID;

	/**
	 * The ID of the associated Person
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
	 * Instantiates a new Event result.
	 *
	 * @param event an event object
	 */
	public EventResult(Event event) {
		this.associatedUsername = event.getUsername();
		this.eventID = event.getEventID();
		this.personID = event.getPersonID();
		this.latitude = event.getLatitude();
		this.longitude = event.getLongitude();
		this.country = event.getCountry();
		this.city = event.getCity();
		this.eventType = event.getEventType();
		this.year = event.getYear();
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
