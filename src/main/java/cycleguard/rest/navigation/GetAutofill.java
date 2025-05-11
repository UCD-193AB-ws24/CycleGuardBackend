package cycleguard.rest.navigation;

import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import cycleguard.auth.AccessTokenManager;
import cycleguard.database.packs.PackData;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
public class GetAutofill {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private GeoApiContextProvider geoApiContextProvider;

	/**
	 * Retrieves user's current pack.
	 * @return {@link PackData}, null if doesn't exist
	 */
	@PostMapping("/navigation/getAutofill")
	public LocationList getAutofill(@RequestHeader("Token") String token, HttpServletResponse response,
	                        @NonNull @RequestBody AutofillBody body)
			throws IOException, InterruptedException, ApiException {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		var sessionToken = new PlaceAutocompleteRequest.SessionToken(token);
		AutocompletePrediction[] res = PlacesApi.placeAutocomplete(geoApiContextProvider.getContext(),
						body.input, sessionToken)
				.await();

		return new LocationList(res);
	}

	public static class AutofillBody {
		public String input;

		@Override
		public String toString() {
			return "AutofillBody{" +
					"input='" + input + '\'' +
					'}';
		}
	}

	public static class LocationList {
		public List<String> results;

		public LocationList(AutocompletePrediction[] predictions) {
			results = Arrays.stream(predictions).map(p->p.description).toList();
		}


		@Override
		public String toString() {
			return "LocationList{" +
					"results=" + results +
					'}';
		}
	}
}

