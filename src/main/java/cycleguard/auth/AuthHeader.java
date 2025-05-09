package cycleguard.auth;

/**
 * Header value for authentication, used to verify user authentication.
 */
public class AuthHeader {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
