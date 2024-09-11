package bantads.airline.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import bantads.airline.collection.User;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByLogin(String login);// in UserService
}
