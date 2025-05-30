package cycleguard.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountServiceTest {
	@Autowired
	private AccountService accountService;

	@Test
	void accountExists() {
		assert !accountService.accountExists(null);
		assert !accountService.accountExists("");
		assert !accountService.accountExists("javagod12");
		assert accountService.accountExists("javagod123");
	}

	@Test
	void isValidLogin() {
		var javagod = new AccountCredentials();
		javagod.setUsername("javagod123");
		javagod.setPassword("");
		assert !accountService.isValidLogin(javagod);
		javagod.setPassword("c++sucks");
		assert accountService.isValidLogin(javagod);
	}
}