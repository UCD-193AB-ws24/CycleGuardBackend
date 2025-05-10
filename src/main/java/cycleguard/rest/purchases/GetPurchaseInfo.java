package cycleguard.rest.purchases;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static cycleguard.database.accessor.PurchaseInfoAccessor.PurchaseInfo;

@RestController
public class GetPurchaseInfo {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;

	/**
	 * Endpoint for retrieving purchase information: owned items split into categories, as well as CycleCoins.
	 * @return Non-null {@link PurchaseInfo}
	 */
	@GetMapping("/purchaseInfo/getPurchaseInfo")
	public PurchaseInfo getPurchaseInfo(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		PurchaseInfo purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);

		return purchaseInfo;
	}
}
