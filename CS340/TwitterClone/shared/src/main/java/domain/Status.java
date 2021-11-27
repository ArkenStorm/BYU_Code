package domain;

import java.util.*;

public class Status {
	private User owner;
	private String status;
	private String timestamp;
	private Long statusDate;
	private List<String> months = new ArrayList<>(Arrays.asList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
			"Aug", "Sep", "Oct", "Nov", "Dec"));

	public Status(User owner, String status) {
		this.owner = owner;
		this.status = status;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		statusDate = calendar.getTimeInMillis();
		timestamp = months.get(Calendar.MONTH) + " " + Calendar.DAY_OF_MONTH;
	}

	public Status() {}

	public User getOwner() {
		return owner;
	}

	public String getStatus() {
		return status;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public Long getDate() {
		return statusDate;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public void setDate(Long date) {
		this.statusDate = date;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Status status1 = (Status) o;
		return owner.equals(status1.owner) &&
				status.equals(status1.status);
	}

	@Override
	public int hashCode() {
		return Objects.hash(owner, status);
	}

	@Override
	public String toString() {
		return "Status{" +
				"owner=" + owner +
				", status='" + status + '\'' +
				'}';
	}
}
