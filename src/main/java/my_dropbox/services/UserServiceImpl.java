package my_dropbox.services;

import java.util.Optional;

import my_dropbox.entities.User;
import my_dropbox.repositories.UserRepository;
import my_dropbox.repositories.UserRepositoryImpl;
import my_dropbox.services.exception.UserAlreadyExistException;
import my_dropbox.services.exception.UserLoggedInException;
import my_dropbox.services.exception.UserLoginInvalidException;
import my_dropbox.services.exception.UserNotLoggedInException;

public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	private Optional<User> currentUser;

	public UserServiceImpl() {
		userRepository = new UserRepositoryImpl();
		currentUser = Optional.empty();
	}

	@Override
	public void register(User user) {
		if (userRepository.find(user.getUsername()).isPresent()) {
			throw new UserAlreadyExistException();
		} else {
			userRepository.create(user);
		}
	}

	@Override
	public User getCurrentUser() {
		return currentUser.orElseThrow(UserNotLoggedInException::new);
	}

	@Override
	public void login(String username, String password) {
		if (currentUser.isPresent()) {
			throw new UserLoggedInException();
		} else {
			Optional<User> user = userRepository.find(username);
			if (user.isPresent() && isPasswordValid(user.get(), password)) {
				currentUser = user;
			} else {
				throw new UserLoginInvalidException();
			}
		}
	}

	private boolean isPasswordValid(User user, String password) {
		return user.getPassword().equals(password);
	}

	@Override
	public void logoff() {
		if(currentUser.isPresent()) {
			currentUser = Optional.empty();
		} else {
			throw new UserNotLoggedInException();
		}
	}

	@Override
	public boolean isLoggedIn() {
		return currentUser.isPresent();
	}
}
