package cycleguard.rest.account;

import cycleguard.auth.AccountCredentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CheckUsernameTest {
	@Autowired
	private CheckUsername checkUsername;

	@Test
	void checkUsername() {
		System.out.println("In checkUsername test");
		assert(checkUsername.userCredentialsAccessor != null);

		var nullFields = new AccountCredentials();
		assert (!checkUsername.checkUsername(nullFields));

		var nullPassword = new AccountCredentials();
		nullPassword.setUsername("NULLUSERNAME");
		assert (!checkUsername.checkUsername(nullPassword));

		var existingAccount = new AccountCredentials();
		existingAccount.setUsername("javagod123");
		assert (checkUsername.checkUsername(existingAccount));
	}
}