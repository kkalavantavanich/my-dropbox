package my_dropbox.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import my_dropbox.aws.dynamo.DynamoClient;
import my_dropbox.aws.dynamo.DynamoClientImpl;
import my_dropbox.aws.dynamo.DynamoTable;
import my_dropbox.entities.User;

public class UserRepositoryImpl implements UserRepository {

	private static final String USER_TABLE_NAME = "my-dropbox-users";
	private static final String USER_TABLE_KEY = "username";

	private DynamoClient dynamoClient;
	private DynamoTable userTable;

	public UserRepositoryImpl() {
		dynamoClient = new DynamoClientImpl();
		userTable = dynamoClient.createTable(USER_TABLE_NAME, USER_TABLE_KEY);
	}

	@Override
	public void create(User user) {
		Map<String, AttributeValue> userItem = new HashMap<>();
		userItem.put("username", new AttributeValue(user.getUsername()));
		userItem.put("password", new AttributeValue(user.getPassword()));
		userItem.put("firstName", new AttributeValue(user.getFirstName()));
		userItem.put("lastName", new AttributeValue(user.getLastName()));
		userTable.putItem(userItem);
	}

	@Override
	public Optional<User> find(String username) {
		Map<String, AttributeValue> userQuery = new HashMap<>();
		userQuery.put("username", new AttributeValue(username));
		Map<String, AttributeValue> userResult = userTable.getItem(userQuery);
		if (userResult == null) {
			return Optional.empty();
		}
		User user = new User();
		user.setUsername(userResult.get("username").getS());
		user.setPassword(userResult.get("password").getS());
		user.setFirstName(userResult.get("firstName").getS());
		user.setLastName(userResult.get("lastName").getS());
		return Optional.of(user);
	}

	@Override
	public void delete(String username) {
		Map<String, AttributeValue> userQuery = new HashMap<>();
		userQuery.put("username", new AttributeValue(username));
		userTable.deleteItem(userQuery);
	}
}
