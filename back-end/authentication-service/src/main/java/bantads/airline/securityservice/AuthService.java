package bantads.airline.securityservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bantads.airline.collection.User;
import bantads.airline.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired//.config bean
    private PasswordEncoder passwordEncoder;

    public String saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "user added to the system";
    }
}
