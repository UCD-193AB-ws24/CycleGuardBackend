package cycleguard.rest;

import cycleguard.database.accessor.AuthTokenAccessor;
import cycleguard.database.entry.UserInfo;
import cycleguard.database.accessor.UserInfoAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
public class CycleCoins {
	@Autowired
	UserInfoAccessor userInfoAccessor;
//	@Autowired
//	AuthTokenAccessor authTokenAccessor;
	@GetMapping("/cyclecoins/getcount")
	public long getCycleCoins() {
		System.out.println("/cyclecoins/getcount called");
		UserInfo info = userInfoAccessor.getEntry("javagod123");

		return info.getCycleCoins();
	}

	@PostMapping("/cyclecoins/add")
	public long addCycleCoins(@NonNull @RequestBody AddCycleCoinsBody body) {
		System.out.println("Earning " + body.coins + " coins...");


		UserInfo info = userInfoAccessor.getEntry("javagod123");
		if (info == null) {
			info = new UserInfo();
			info.setUsername("javagod123");
			info.setEmail("jhfeng@ucdavis.edu");
		}

		info.setCycleCoins(info.getCycleCoins() + body.coins);

		userInfoAccessor.setEntry(info);

		return info.getCycleCoins();
	}

	private static class AddCycleCoinsBody {
		public int coins;
	}
}
