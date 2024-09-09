package bantads.airline.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import bantads.airline.collection.User;

public interface UserRepository extends MongoRepository<User, String> {

}
