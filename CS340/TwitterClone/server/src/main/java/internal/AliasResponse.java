package internal;

import java.util.List;

public class AliasResponse {
	private List<String> aliases;
	private boolean hasMorePages = false;

	public AliasResponse(List<String> aliases, boolean hasMorePages) {
		this.aliases = aliases;
		this.hasMorePages = hasMorePages;
	}

	public AliasResponse() {}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public boolean isHasMorePages() {
		return hasMorePages;
	}

	public void setHasMorePages(boolean hasMorePages) {
		this.hasMorePages = hasMorePages;
	}
}
