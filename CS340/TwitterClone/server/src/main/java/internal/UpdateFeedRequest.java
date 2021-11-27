package internal;

import domain.Status;

import java.util.List;

public class UpdateFeedRequest {
	List<String> aliases;
	Status status;

	public UpdateFeedRequest(List<String> aliases, Status status) {
		this.aliases = aliases;
		this.status = status;
	}

	public UpdateFeedRequest() {}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
