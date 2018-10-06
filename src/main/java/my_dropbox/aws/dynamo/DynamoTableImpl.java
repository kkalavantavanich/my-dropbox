package my_dropbox.aws.dynamo;

import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

public class DynamoTableImpl implements DynamoTable {
	private AmazonDynamoDB dynamoDbClient;
	private String name;

	public DynamoTableImpl(AmazonDynamoDB dynamoDbClient) {
		this.dynamoDbClient = dynamoDbClient;
	}

	@Override
	public void destroy() {
		try {
			dynamoDbClient.deleteTable(name);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}
	}

	@Override
	public Map<String, AttributeValue> getItem(Map<String, AttributeValue> key) {
		return getItem(key, null);
	}

	@Override
	public Map<String, AttributeValue> getItem(Map<String, AttributeValue> key, String projectionExpression) {

		GetItemRequest request;
		if (projectionExpression != null) {
			request = new GetItemRequest()
					.withKey(key)
					.withTableName(name)
					.withProjectionExpression(projectionExpression);
		} else {
			request = new GetItemRequest()
					.withKey(key)
					.withTableName(name);
		}

		Map<String, AttributeValue> item = null;
		try {
			item = dynamoDbClient.getItem(request).getItem();
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		}
		return item;
	}

	@Override
	public void putItem(Map<String, AttributeValue> item) {
		try {
			dynamoDbClient.putItem(name, item);
		} catch (ResourceNotFoundException e) {
			System.err.format("Error: The table \"%s\" can't be found.\n", name);
			System.exit(1);
		} catch (AmazonServiceException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	@Override
	public void updateItem(Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> values) {
		try {
			dynamoDbClient.updateItem(name, key, values);
		} catch (ResourceNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (AmazonServiceException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}

	@Override
	public void deleteItem(Map<String, AttributeValue> item) {
		try {
			dynamoDbClient.deleteItem(name, item);
		} catch (ResourceNotFoundException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		} catch (AmazonServiceException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}


	@Override
	public void setName(String name) {
		this.name = name;
	}
}
