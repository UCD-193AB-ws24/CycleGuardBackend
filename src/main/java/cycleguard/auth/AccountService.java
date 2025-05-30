package cycleguard.auth;

import cycleguard.database.accessor.UserCredentialsAccessor;
import cycleguard.database.accessor.UserCredentialsAccessor.HashedUserCredentials;
import cycleguard.database.stats.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service wrapper for creating and authenticating accounts.
 */
@Service
public class AccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCredentialsAccessor userCredentialsAccessor;

	@Autowired
	private UserStatsService userStatsService;

	/**
	 * Return if an account exists for a username.
	 * @param username User to check existence
	 * @return If the user exists or not
	 */
	public boolean accountExists(String username) {
		return userCredentialsAccessor.hasEntry(username);
	}

	/**
	 * Creates a user from given credentials. Does not put into database.
	 * @param credentials Username and password of user
	 * @return {@link HashedUserCredentials} to be inserted into database
	 */
	private HashedUserCredentials createHashedUser(AccountCredentials credentials) {
		HashedUserCredentials hashedUserCredentials = new HashedUserCredentials();
		String hashedPassword = passwordEncoder.encode(credentials.getPassword());

		hashedUserCredentials.setUsername(credentials.getUsername());
		hashedUserCredentials.setHashedPassword(hashedPassword);

		return hashedUserCredentials;
	}

	/**
	 * Insert a {@link HashedUserCredentials} into the database from given plaintext credentials.
	 * @param credentials Username and password of user
	 */
	public void createAccount(AccountCredentials credentials) {
		if (credentials==null || credentials.getUsername()==null || credentials.getPassword()==null) return;
		HashedUserCredentials hashedUserCredentials = createHashedUser(credentials);
		userCredentialsAccessor.setEntry(hashedUserCredentials);

		userStatsService.createUser(credentials.getUsername());
	}

	/**
	 * Check if given credentials are valid,
	 * by matching the encoded password from the database to the plaintext password.
	 * @param accountCredentials User's plaintext credentials
	 * @return True if match, false if not
	 */
	public boolean isValidLogin(AccountCredentials accountCredentials) {
		HashedUserCredentials hashedUserCredentials = userCredentialsAccessor.getEntry(accountCredentials.getUsername());

//		Check if username is valid
		if (hashedUserCredentials == null) return false;

//		Check if password matches
		if (!passwordEncoder.matches(accountCredentials.getPassword(), hashedUserCredentials.getHashedPassword()))
			return false;

		return true;
	}
}
