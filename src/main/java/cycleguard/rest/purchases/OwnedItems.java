package cycleguard.rest.purchases;

import cycleguard.auth.AccessTokenManager;
import cycleguard.auth.AccountCredentials;
import cycleguard.database.accessor.PurchaseInfoAccessor;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * Endpoint for a user to create an account.
 * Requires {@link AccountCredentials} as body.
 */
@RestController
public final class OwnedItems {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private PurchaseInfoAccessor purchaseInfoAccessor;
	@Autowired
	private ItemInfoService itemInfoService;

	@GetMapping("/purchaseInfo/ownedItems")
	public List<String> buy(@RequestHeader("Token") String token, HttpServletResponse response) {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		PurchaseInfoAccessor.PurchaseInfo purchaseInfo = purchaseInfoAccessor.getEntryOrDefaultBlank(username);

		return purchaseInfo.getThemesOwned();
	}

	private static final class ItemToBuy {
		private String item;

		public String getItem() {
			return item;
		}

		public void setItem(String item) {
			this.item = item;
		}
	}
}
