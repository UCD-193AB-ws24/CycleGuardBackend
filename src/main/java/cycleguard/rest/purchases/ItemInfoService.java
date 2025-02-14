package cycleguard.rest.purchases;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public final class ItemInfoService {
	@Bean
	public ItemInfo itemInfo() {
		return new ItemInfo();
	}
}
