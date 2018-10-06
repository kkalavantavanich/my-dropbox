package my_dropbox.aws.dynamo;

import java.util.HashMap;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.junit.Test;

public class DynamoClientTest {

	@Test
	public void testDynamoClient() {
		DynamoClient dynamoClient = new DynamoClientImpl();
		DynamoTable table = dynamoClient.createTable("test-table", "id");
		dynamoClient.getTableNames().forEach(System.out::println);
		HashMap<String, AttributeValue> item = new HashMap<>();
		item.put("id", new AttributeValue("12381"));
		item.put("First Name", new AttributeValue("John"));
		item.put("Last Name", new AttributeValue("Doe"));
		table.putItem(item);
		table.destroy();
	}
}
