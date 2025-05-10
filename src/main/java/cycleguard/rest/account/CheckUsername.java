package cycleguard.rest.account;

import cycleguard.auth.AccountCredentials;
import cycleguard.database.accessor.UserCredentialsAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckUsername {
	@Autowired
	UserCredentialsAccessor userCredentialsAccessor;

	/**
	 * Endpoint to check if a username is taken.
	 * @param credentials {@link AccountCredentials} with blank password: username to check
	 * @return True if username is taken, false if username is available
	 */
	@GetMapping("/account/checkUsername")
	public boolean checkUsername(@RequestBody @NonNull AccountCredentials credentials) {
		return userCredentialsAccessor.hasEntry(credentials.getUsername());
	}
}
