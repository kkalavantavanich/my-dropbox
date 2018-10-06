package my_dropbox.repositories;

import java.util.Optional;

import my_dropbox.entities.User;

public interface UserRepository {
	void create(User user);
	Optional<User> find(String username);
	void delete(String username);
}
