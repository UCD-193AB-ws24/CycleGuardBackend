package cycleguard.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccessTokenManagerTest {
	@Autowired
	private AccessTokenManager accessTokenManager;

	@Test
	void getUsernameFromToken() {
		assert accessTokenManager.getUsernameFromToken(null)==null;
		assert accessTokenManager.getUsernameFromToken("")==null;
		assert accessTokenManager.getUsernameFromToken("ugu3PhIxF7PCB1wN").equals("javagod123");
//		Check cache
		assert accessTokenManager.getUsernameFromToken("ugu3PhIxF7PCB1wN").equals("javagod123");

	}

	@Test
	void setRandomNewToken() {
		accessTokenManager.setRandomNewToken("javagod123");
	}
}