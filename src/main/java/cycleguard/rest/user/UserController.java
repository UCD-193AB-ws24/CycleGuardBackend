package cycleguard;

import cycleguard.database.accessor.UserProfileAccessor;
import cycleguard.database.accessor.UserProfileAccessor.UserProfile;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {
    private final UserProfileAccessor userProfileAccessor;

    public UserController(UserProfileAccessor userProfileAccessor) {
        this.userProfileAccessor = userProfileAccessor;
    }

    // API to fetch all users
    @GetMapping("/users/all")
    public List<UserProfile> getAllUsers() {
        return userProfileAccessor.getAllUsers();
    }
}
