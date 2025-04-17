package cycleguard.rest.purchases;

import cycleguard.auth.AccessTokenManager;
import cycleguard.auth.AccountCredentials;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import cycleguard.database.achievements.AchievementInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoint for a user to create an account.
 * Requires {@link AccountCredentials} as body.
 */
@RestController
public final class BuyTheme {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;
	@Autowired
	private ItemInfoService itemInfoService;
	@Autowired
	private AchievementInfoService achievementInfoService;

	@PostMapping("/purchaseInfo/buyTheme")
	public String buy(@RequestHeader("Token") String token, HttpServletResponse response,
	                      @RequestBody @NonNull ItemToBuy itemToBuy) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		String item = itemToBuy.getItem();

		PurchaseInfoAccessor.PurchaseInfo purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
		if (purchaseInfo.getThemesOwned().contains(item)) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return "ALREADY OWNED";
		}
		if (purchaseInfo.getCycleCoins() < 10) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return "NOT ENOUGH COINS: NEED 10";
		}

		purchaseInfo.setCycleCoins(purchaseInfo.getCycleCoins()-10);
		purchaseInfo.getThemesOwned().add(item);

		purchaseInfoAccessor.setEntry(purchaseInfo);
		achievementInfoService.processAchievements(username);

		return "OK";
	}
}
