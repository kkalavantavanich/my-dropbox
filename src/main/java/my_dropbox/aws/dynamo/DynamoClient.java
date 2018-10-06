package my_dropbox.aws.dynamo;

import java.util.List;

public interface DynamoClient {
	DynamoTable createTable(String tableName, String keyName);
	List<String> getTableNames();
}
