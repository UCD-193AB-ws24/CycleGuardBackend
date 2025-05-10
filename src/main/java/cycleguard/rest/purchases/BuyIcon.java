package cycleguard.rest.purchases;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import cycleguard.database.achievements.AchievementInfoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class BuyIcon {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;
	@Autowired
	private AchievementInfoService achievementInfoService;

	/**
	 * Endpoint for purchasing a user icon.
	 * @param itemToBuy Item to add to the user's items. Each item costs 50 CycleCoins.
	 * @return OK or error code 409
	 */
	@PostMapping("/purchaseInfo/buyIcon")
	public String buy(@RequestHeader("Token") String token, HttpServletResponse response,
	                      @RequestBody @NonNull ItemToBuy itemToBuy) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		String item = itemToBuy.getItem();

		PurchaseInfoAccessor.PurchaseInfo purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
		if (purchaseInfo.getIconsOwned().contains(item)) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return "ALREADY OWNED";
		}
		if (purchaseInfo.getCycleCoins() < 30) {
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return "NOT ENOUGH COINS: NEED 30";
		}

		purchaseInfo.setCycleCoins(purchaseInfo.getCycleCoins()-30);
		purchaseInfo.getIconsOwned().add(item);

		purchaseInfoAccessor.setEntry(purchaseInfo);
		achievementInfoService.processAchievements(username);

		return "OK";
	}
}
