package bantads.airline.securityservice;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import bantads.airline.collection.User;
import bantads.airline.repository.UserRepository;
import bantads.airline.sagas.commands.CreateUserCommand;
import bantads.airline.sagas.events.UserCreatedEvent;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired // in .config bean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public String saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "User added to the system";
    }

    public UserCreatedEvent saveNewUserRequest(CreateUserCommand newUserRequest) {

        String pswd = generateRamdomPassword();

        User user = User.builder()
                .name(newUserRequest.getName())
                .login(newUserRequest.getEmail())
                .password(passwordEncoder.encode(pswd))
                .role("CLIENT")
                .userStatus("ACTIVE")
                .build();

        User newUser = userRepository.save(user);

        UserCreatedEvent event = UserCreatedEvent.builder()
                .userId(newUser.getId())
                .userPswd(pswd)
                .messageType("UserCreatedEvent")
                .build();

        return event;
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    private String generateRamdomPassword() {

        String CHARACTERS = "0123456789";

        int STRING_LENGTH = 4;

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(STRING_LENGTH);

        for (int i = 0; i < STRING_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();

    }

}
