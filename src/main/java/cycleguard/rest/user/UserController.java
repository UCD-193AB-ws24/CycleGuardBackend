package cycleguard.rest.user;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.accessor.UserProfileAccessor.UserProfile;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user") // Consistent naming
public class UserController {
    @Autowired
    private AccessTokenManager accessTokenManager; // Add authentication manager

    private final UserProfileAccessor userProfileAccessor;

    public UserController(UserProfileAccessor userProfileAccessor) {
        this.userProfileAccessor = userProfileAccessor;
    }

    @GetMapping("/all")
    public List<UserProfile> getAllUsers(@RequestHeader("Token") String token, HttpServletResponse response) {
        String username = accessTokenManager.getUsernameFromToken(token);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        return userProfileAccessor.getAllUsers();
    }
}
