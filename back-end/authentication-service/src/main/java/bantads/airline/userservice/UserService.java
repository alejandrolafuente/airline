package bantads.airline.userservice;

import java.util.Optional;

import bantads.airline.collection.User;

public interface UserService {

    Optional<User> findBylogin(String login);
}
