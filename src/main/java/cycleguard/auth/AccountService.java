package cycleguard.auth;

import cycleguard.database.accessor.UserCredentialsAccessor;
import cycleguard.database.entry.HashedUserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserCredentialsAccessor userCredentialsAccessor;

	private HashedUserCredentials createHashedUser(AccountCredentials credentials) {
		HashedUserCredentials hashedUserCredentials = new HashedUserCredentials();
		String hashedPassword = passwordEncoder.encode(credentials.getPassword());

		hashedUserCredentials.setUsername(credentials.getUsername());
		hashedUserCredentials.setHashedPassword(hashedPassword);

		return hashedUserCredentials;
	}

	public void createAccount(AccountCredentials credentials) {
		System.out.println(passwordEncoder.encode(credentials.getPassword()));
		HashedUserCredentials hashedUserCredentials = createHashedUser(credentials);
		userCredentialsAccessor.setEntry(hashedUserCredentials);
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
