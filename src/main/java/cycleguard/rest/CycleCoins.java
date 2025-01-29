package cycleguard.rest;

import cycleguard.database.UserInfo;
import cycleguard.database.UserInfoAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
public class CycleCoins {
	@Autowired
	UserInfoAccessor userInfoAccessor;
	@GetMapping("/cyclecoins/getcount")
	public long getCycleCoins() {
		UserInfo info = userInfoAccessor.getEntry(69420);

		return info.getCycleCoins();
	}

	@PostMapping("/cyclecoins/add")
	public long addCycleCoins(@NonNull @RequestBody AddCycleCoinsBody body) {
		System.out.println("Earning " + body.coins + " coins...");


		UserInfo info = userInfoAccessor.getEntry(69420);
		if (info == null) info = new UserInfo();

		info.setCycleCoins(info.getCycleCoins() + body.coins);
		userInfoAccessor.setEntry(info);

		return info.getCycleCoins();
	}

	private static class AddCycleCoinsBody {
		public int coins;
	}
}
