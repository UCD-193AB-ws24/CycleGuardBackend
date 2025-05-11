package cycleguard.rest.navigation;

import com.google.maps.GeoApiContext;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class GeoApiContextProvider {
	private GeoApiContext context;
	@PostConstruct
	private void createGetApiContext() {
		context = new GeoApiContext.Builder()
				.apiKey(System.getenv("GOOGLE_API_KEY"))
				.build();
	}

	public GeoApiContext getContext() {
		return context;
	}
}