package bantads.airline.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bantads.airline.collection.User;
import bantads.airline.dto.AuthRequestDTO;
import bantads.airline.dto.LoginReturnDTO;
import bantads.airline.securityservice.AuthService;
import bantads.airline.userservice.UserService;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired // in .config bean
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody User user) {
        return authService.saveUser(user);
    }

    @PostMapping("/login")
    public LoginReturnDTO doLogin(@RequestBody AuthRequestDTO authRequestDTO) {

        Authentication authenticate = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(authRequestDTO.getLogin(),
                                authRequestDTO.getPassword()));

        if (authenticate.isAuthenticated()) {

            String userToken = authService.generateToken(authRequestDTO.getLogin());

            Optional<User> optionalUser = userService.findBylogin(authRequestDTO.getLogin());

            if (optionalUser.isPresent()) {

                User user = optionalUser.get();

                if ("ACTIVE".equals(user.getUserStatus())) {
                    LoginReturnDTO response = LoginReturnDTO.builder()
                            .userId(user.getId())
                            .token(userToken)
                            .name(user.getName())
                            .login(user.getLogin())
                            .role(user.getRole())
                            .build();

                    return response;
                } else {
                    throw new RuntimeException("Invalid access");
                }

            } else {
                // throw new RuntimeException("User not found");
                throw new RuntimeException("Invalid access");
            }
        } else {
            throw new RuntimeException("Invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestHeader("x-access-token") String token) {
        System.out.println("Request has come with token: " + token);
        authService.validateToken(token);
        return "Token is valid";
    }
}
