package cycleguard.rest.account;

import cycleguard.auth.AccountCredentials;
import cycleguard.auth.AccountService;
import cycleguard.database.accessor.UserCredentialsAccessor;
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
	@Autowired
	private UserCredentialsAccessor userCredentialsAccessor;
	@Autowired
	private CheckUsername checkUsername;
	@Autowired
	private Login login;

	/**
	 * Creates an account and returns an access token, or returns DUPLICATE.
	 * @param credentials Plaintext username and password
	 * @return Access token to put in headers, or DUPLICATE
	 */
	@PostMapping("/account/create")
	public String createAccount(@RequestBody @NonNull AccountCredentials credentials) {
		if (checkUsername.checkUsername(credentials)) return "DUPLICATE";

		accountService.createAccount(credentials);

		return login.login(credentials);
	}
}
