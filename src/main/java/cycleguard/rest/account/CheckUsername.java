package cycleguard.rest.account;

import cycleguard.auth.AccountCredentials;
import cycleguard.database.accessor.UserCredentialsAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint to check if a username is available.
 * Requires {@link AccountCredentials} with blank password as body.
 */
@RestController
public class CheckUsername {
	@Autowired
	UserCredentialsAccessor userCredentialsAccessor;

	@GetMapping("/account/checkUsername")
	public boolean checkUsername(@RequestBody @NonNull AccountCredentials credentials) {
		return userCredentialsAccessor.hasEntry(credentials.getUsername());
	}
}
