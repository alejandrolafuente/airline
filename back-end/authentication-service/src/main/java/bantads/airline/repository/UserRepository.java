package bantads.airline.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import bantads.airline.collection.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByLogin(String login);// in UserService
}
