package cycleguard.rest.navigation;

import com.google.maps.DirectionsApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import cycleguard.auth.AccessTokenManager;
import cycleguard.database.packs.PackData;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GetRoute {
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private GeoApiContextProvider geoApiContextProvider;

	/**
	 * Retrieves user's current pack.
	 * @return {@link PackData}, null if doesn't exist
	 */
	@PostMapping("/navigation/getRoute")
	public RouteLatLng getRoute(@RequestHeader("Token") String token, HttpServletResponse response,
	                        @NonNull @RequestBody RouteBody body)
			throws IOException, InterruptedException, ApiException {
		String username = accessTokenManager.getUsernameFromToken(token);
		if (username == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}

		DirectionsResult dir = DirectionsApi.getDirections(geoApiContextProvider.getContext(),
						body.getOrigin(),
						body.getDestination())
				.mode(TravelMode.BICYCLING)
				.await();


		DirectionsRoute route = dir.routes[0];
		List<LatLng> path = route.overviewPolyline.decodePath();

		return new RouteLatLng(path);
	}

	public static class RouteBody {
		public double startLat, startLng;
		public String destination;

		public String getOrigin() {
			return String.format("%f,%f", startLat, startLng);
		}

		public String getDestination() {
			return destination;
		}

		@Override
		public String toString() {
			return "RouteBody{" +
					"startLat=" + startLat +
					", startLng=" + startLng +
					", destination='" + destination + '\'' +
					'}';
		}
	}

	public static class RouteLatLng {
		public RouteLatLng(){}

		public RouteLatLng(ArrayList<Double> latitudes, ArrayList<Double> longitudes) {
			this.latitudes = latitudes;
			this.longitudes = longitudes;
		}

		public RouteLatLng(List<LatLng> list) {
			latitudes = new ArrayList<>(list.size());
			longitudes = new ArrayList<>(list.size());

			for (var coord : list) {
				latitudes.add(coord.lat);
				longitudes.add(coord.lng);
			}
		}

		public ArrayList<Double> latitudes = new ArrayList<>(), longitudes = new ArrayList<>();

		@Override
		public String toString() {
			return "RouteLatLng{" +
					"latitudes=" + latitudes +
					", longitudes=" + longitudes +
					'}';
		}
	}
}

