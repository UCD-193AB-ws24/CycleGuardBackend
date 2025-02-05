package cycleguard.auth;

import cycleguard.database.entry.HashedUserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public HashedUserCredentials createHashedUser(AccountCredentials credentials) {
		HashedUserCredentials hashedUserCredentials = new HashedUserCredentials();
		String hashedPassword = passwordEncoder.encode(credentials.getPassword());

		hashedUserCredentials.setUsername(credentials.getUsername());
		hashedUserCredentials.setHashedPassword(hashedPassword);

		return hashedUserCredentials;
	}
}
