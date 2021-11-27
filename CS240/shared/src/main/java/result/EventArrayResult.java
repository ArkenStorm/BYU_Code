package result;

import model.Event;

import java.util.List;

/**
 * An Array of Events.
 */
public class EventArrayResult extends Result {
	/**
	 * The list of Events
	 */
	private List<Event> data;

	/**
	 * Instantiates a new Event array result.
	 *
	 * @param data the data
	 */
	public EventArrayResult(List<Event> data) {
		this.data = data;
	}

	/**
	 * Gets data.
	 *
	 * @return the data
	 */
	public List<Event> getData() {
		return data;
	}

	/**
	 * Sets data.
	 *
	 * @param data the data
	 */
	public void setData(List<Event> data) {
		this.data = data;
	}
}
