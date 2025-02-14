package cycleguard.rest.purchases;


import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import cycleguard.database.accessor.UserInfoAccessor;
import cycleguard.database.accessor.UserInfoAccessor.UserInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import static cycleguard.database.accessor.PurchaseInfoAccessor.*;

@RestController
public class CycleCoins {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;
	@GetMapping("/purchaseInfo/getCycleCoins")
	public long getCycleCoins(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return -1;
		}

		PurchaseInfo purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);

		return purchaseInfo.getCycleCoins();
	}

	@PostMapping("/purchaseInfo/addCycleCoins")
	public long addCycleCoins(@RequestHeader("Token") String token, HttpServletResponse response,
	                          @NonNull @RequestBody AddCycleCoinsBody body) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return -1;
		}

		PurchaseInfo purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
		purchaseInfo.setCycleCoins(purchaseInfo.getCycleCoins() + body.coins);

		purchaseInfoAccessor.setEntry(purchaseInfo);

		return purchaseInfo.getCycleCoins();
	}

	private static class AddCycleCoinsBody {
		public int coins;
	}
}
