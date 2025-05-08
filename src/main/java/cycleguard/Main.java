package cycleguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Driver class for Spring Boot application. Disables auto security for a custom token system.
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableScheduling
//@SpringBootApplication
public class Main {
	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}
}
