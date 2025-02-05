package cycleguard.auth;

import cycleguard.database.entry.HashedUserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
	@Autowired
	private PasswordEncoder passwordEncoder;

	public void createHashedUser(AccountCredentials credentials) {
		HashedUserCredentials hashedUserCredentials = new HashedUserCredentials();

	}
}
