package cycleguard.auth;

import cycleguard.database.accessor.AuthTokenAccessor;
import cycleguard.database.accessor.AuthTokenAccessor.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class AccessTokenManager {
	@Autowired
	private AccountService accountService;
	@Autowired
	private AuthTokenAccessor authTokenAccessor;

	private final SecureRandom random = new SecureRandom();

	/**
	 * Length of generated access tokens.
	 */
	private static final int TOKEN_LENGTH = 16;

	/**
	 * Generated a cryptographically secure {@link String} using the 64 letters:
	 * <br>
	 * [a-z][A-Z][0-9][-][_]
	 * <br><br>
	 * Up to 64^{@link AccessTokenManager#TOKEN_LENGTH} unique tokens are possible.
	 * @return {@link String} with the specified above format.
	 */
	private String getRandomToken() {
		StringBuilder builder = new StringBuilder(TOKEN_LENGTH);
		byte[] arr = new byte[TOKEN_LENGTH];
		random.nextBytes(arr);

		for (byte b : arr) {
			b &= 0x3F;
			char c;
			if (b<10) c = (char) (b+'0');
			else if (b<10+26) c = (char) (b-10+'A');
			else if (b<10+26+26) c = (char) (b-10-26+'a');
			else if (b==62) c = '-';
			else c = '_';
			builder.append(c);
		}

		return builder.toString();
	}

	private String getUnusedToken() {
		String token;
		do {
			token = getRandomToken();
		} while (authTokenAccessor.hasEntry(token));
		return token;
	}

	public String getUsernameFromToken(String token) {
		AuthToken authToken = authTokenAccessor.getEntry(token);
		if (authToken == null) return null;
		return authToken.getUsername();
	}

	public String setRandomNewToken(String username) {
		String token = getUnusedToken();

		AuthToken authToken = new AuthToken();
		authToken.setToken(token);
		authToken.setUsername(username);

		authTokenAccessor.setEntry(authToken);

		return token;
	}
}
