package model;


import java.util.Objects;

/**
 * Used for authenticating a User to use the API.
 */
public class AuthToken {
	/**
	 * The authentication token itself.
	 */
	private String authToken;

	/**
	 * The user associated with the AuthToken.
	 */
	private String username;

	/**
	 * Instantiates a new Auth token.
	 *
	 * @param authToken the auth token
	 * @param username  the username
	 */
	public AuthToken(String authToken, String username) {
		this.authToken = authToken;
		this.username = username;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AuthToken authToken1 = (AuthToken) o;
		return authToken.equals(authToken1.authToken) &&
				username.equals(authToken1.username);
	}

	@Override
	public int hashCode() {
		return Objects.hash(authToken, username);
	}

	/**
	 * Gets auth token.
	 *
	 * @return the auth token
	 */
	public String getAuthToken() {
		return authToken;
	}

	/**
	 * Sets auth token.
	 *
	 * @param authToken the auth token
	 */
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}


	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

}
