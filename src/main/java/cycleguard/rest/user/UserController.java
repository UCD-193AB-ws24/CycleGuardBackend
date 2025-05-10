package cycleguard.rest.user;

import cycleguard.auth.AccessTokenManager;
import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.accessor.UserProfileAccessor.UserProfile;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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

    /**
     * Endpoint for retrieving every single user in the database.
     * @return Non-null {@link UserProfileList} of all users
     */
    @GetMapping("/all")
    public UserProfileList getAllUsers(@RequestHeader("Token") String token, HttpServletResponse response) {
        String username = accessTokenManager.getUsernameFromToken(token);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return null;
        }

        return new UserProfileList(username, userProfileAccessor.getAllUsers());
    }
}

class UserProfileList {
    public UserProfileList() {};

    public UserProfileList(String username, List<UserProfile> users) {
        this.username = username;
        this.users = users;
    }

    public String username = "";
    public List<UserProfile> users = new ArrayList<>();
}
