package cycleguard.database.accessor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserCredentialsAccessorTest {
	@Autowired
	private UserCredentialsAccessor userCredentialsAccessor;

	@Test
	void clearOldCacheEntries() {
	}

	@Test
	void subscribeToCache() {
	}

	@Test
	void getClient() {
	}

	@Test
	void getEntry() {
	}

	@Test
	void getEntryOrDefaultBlank() {
	}

	@Test
	void hasEntry() {
		assert !userCredentialsAccessor.hasEntry(null);
		assert !userCredentialsAccessor.hasEntry("NULLUSER");
		assert userCredentialsAccessor.hasEntry("javagod123");
	}

	@Test
	void setEntry() {
	}

	@Test
	void deleteEntry() {
	}

	@Test
	void getTableInstance() {
	}

	@Test
	void getBlankEntry() {
	}
}