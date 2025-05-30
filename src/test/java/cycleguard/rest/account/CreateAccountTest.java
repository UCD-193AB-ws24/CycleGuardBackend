package cycleguard.rest.account;

import cycleguard.auth.AccountCredentials;
import cycleguard.auth.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CreateAccountTest {
	@Autowired
	private AccountService accountService;
	@Autowired
	private CreateAccount createAccount;
	@Test
	void createAccount() {
		createAccount.createAccount(null);
		var nullCred = new AccountCredentials();
		createAccount.createAccount(nullCred);
		nullCred.setUsername("");
		createAccount.createAccount(nullCred);

		var javagod = new AccountCredentials();
		javagod.setUsername("javagod123");
		javagod.setPassword("c++sucks");
		createAccount.createAccount(javagod);

		assert !accountService.accountExists(null);
		assert accountService.accountExists("javagod123");
	}
}