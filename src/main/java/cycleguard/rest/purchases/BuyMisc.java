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
public final class BuyMisc {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;
	@Autowired
	private AchievementInfoService achievementInfoService;

	/**
	 * Endpoint for purchasing a miscellaneous item.
	 * @param itemToBuy Item to add to the user's items.
	 * @return OK or error code 409
	 */
	@PostMapping("/purchaseInfo/buyMisc")
	public String buy(@RequestHeader("Token") String token, HttpServletResponse response,
	                      @RequestBody @NonNull ItemToBuy itemToBuy) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return "UNAUTHORIZED";
		}

		String item = itemToBuy.getItem();

		PurchaseInfoAccessor.PurchaseInfo purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);
//		if (purchaseInfo.getMiscOwned().contains(item)) {
//			response.setStatus(HttpServletResponse.SC_CONFLICT);
//			return "ALREADY OWNED";
//		}

		boolean bought = false;
		if (item.equals("Rocket Boost")) {
			if (purchaseInfo.getCycleCoins() < 100) {
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				return "NOT ENOUGH COINS: NEED 100";
			}
			purchaseInfo.setCycleCoins(purchaseInfo.getCycleCoins()-100);
			bought = true;
		}
		if (!bought) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return "NOT FOUND";
		}

		if (!purchaseInfo.getMiscOwned().contains(item))
			purchaseInfo.getMiscOwned().add(item);

		purchaseInfoAccessor.setEntry(purchaseInfo);
		achievementInfoService.processAchievements(username);

		return "OK";
	}
}
