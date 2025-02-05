package cycleguard.rest.account;

import cycleguard.auth.AccountService;
import cycleguard.auth.AccountCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint for a user to create an account.
 * Requires {@link AccountCredentials} as body.
 */
@RestController
public final class CreateAccount {
	@Autowired
	private AccountService accountService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/createAccount")
	public long createAccount(@RequestBody @NonNull AccountCredentials credentials) {
		System.out.println(credentials.getUsername());
		System.out.println(credentials.getPassword());

		System.out.println(passwordEncoder.encode(credentials.getPassword()));

		return 69;
	}
}
