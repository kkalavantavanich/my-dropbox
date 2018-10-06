package my_dropbox.aws.dynamo;

import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;

public interface DynamoTable {
	void destroy();
	Map<String, AttributeValue> getItem(Map<String, AttributeValue> key);
	Map<String, AttributeValue> getItem(Map<String, AttributeValue> key, String projection_expression);
	void putItem(Map<String, AttributeValue> item);
	void updateItem(Map<String, AttributeValue> key, Map<String, AttributeValueUpdate> values);
	void deleteItem(Map<String, AttributeValue> item);
	void setName(String name);
}
