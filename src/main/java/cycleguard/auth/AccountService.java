package cycleguard.auth;

import cycleguard.database.accessor.UserCredentialsAccessor;
import cycleguard.database.accessor.UserCredentialsAccessor.HashedUserCredentials;
import cycleguard.database.stats.UserStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCredentialsAccessor userCredentialsAccessor;

	@Autowired
	private UserStatsService userStatsService;

	public boolean accountExists(String username) {
		return userCredentialsAccessor.hasEntry(username);
	}
	private HashedUserCredentials createHashedUser(AccountCredentials credentials) {
		HashedUserCredentials hashedUserCredentials = new HashedUserCredentials();
		String hashedPassword = passwordEncoder.encode(credentials.getPassword());

		hashedUserCredentials.setUsername(credentials.getUsername());
		hashedUserCredentials.setHashedPassword(hashedPassword);

		return hashedUserCredentials;
	}

	public void createAccount(AccountCredentials credentials) {
		HashedUserCredentials hashedUserCredentials = createHashedUser(credentials);
		userCredentialsAccessor.setEntry(hashedUserCredentials);

		userStatsService.createUser(credentials.getUsername());
	}

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
