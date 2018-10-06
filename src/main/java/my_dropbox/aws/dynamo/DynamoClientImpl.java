package my_dropbox.aws.dynamo;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

public class DynamoClientImpl implements DynamoClient {

	private AmazonDynamoDB dynamoDbClient;
	private DynamoDB dynamoDB;

	public DynamoClientImpl() {
		dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.AP_SOUTHEAST_1)
				.withCredentials(new ProfileCredentialsProvider("dynamo-only-access"))
				.build();
		dynamoDB = new DynamoDB(dynamoDbClient);
	}

	public DynamoTable createTable(String tableName, String keyName) {
		if (getTableNames().contains(tableName)) {
			DynamoTable dynamoTable = new DynamoTableImpl(dynamoDbClient);
			dynamoTable.setName(tableName);
			return dynamoTable;
		}

		CreateTableRequest request = new CreateTableRequest()
				.withAttributeDefinitions(new AttributeDefinition(
						keyName, ScalarAttributeType.S))
				.withKeySchema(new KeySchemaElement(keyName, KeyType.HASH))
				.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
				.withTableName(tableName);

		DynamoTable dynamoTable = null;
		try {
			Table table = dynamoDB.createTable(request);
			table.waitForActive();
			dynamoTable = new DynamoTableImpl(dynamoDbClient);
			dynamoTable.setName(tableName);
		} catch (AmazonServiceException e) {
			System.err.println(e.getErrorMessage());
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return dynamoTable;
	}

	@Override
	public List<String> getTableNames() {
		ListTablesRequest request;

		boolean hasMoreTables = true;
		String lastName = null;

		List<String> tableNames = new ArrayList<>();

		while(hasMoreTables) {
			try {
				request = getListTablesRequest(lastName);

				ListTablesResult listTablesResult = dynamoDbClient.listTables(request);
				List<String> partialTableNames = listTablesResult.getTableNames();

				tableNames.addAll(partialTableNames);

				lastName = listTablesResult.getLastEvaluatedTableName();
				if (lastName == null) {
					hasMoreTables = false;
				}

			} catch (AmazonServiceException e) {
				System.err.println(e.getErrorMessage());
			}
		}

		return tableNames;
	}

	private ListTablesRequest getListTablesRequest(String lastName) {
		ListTablesRequest request;
		if (lastName == null) {
			request = new ListTablesRequest().withLimit(10);
		}
		else {
			request = new ListTablesRequest()
					.withLimit(10)
					.withExclusiveStartTableName(lastName);
		}
		return request;
	}
}
