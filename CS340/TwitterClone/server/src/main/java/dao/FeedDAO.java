package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.google.gson.Gson;
import domain.Status;
import internal.UpdateFeedRequest;
import request.StatusRequest;
import response.StatusResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FeedDAO {
	private static final String tableName = "feed";
	private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion("us-east-2")
			.build();
	private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
	private static final Table table = dynamoDB.getTable(tableName);
	private static final String partitionKey = "handle";
	private static final String sortKey = "statusDate";
	private static final String STATUS_KEY = "status";

	public StatusResponse getFeed(StatusRequest request) {
		Gson gson = new Gson();
		HashMap<String, String> nameMap = new HashMap<>();
		nameMap.put("#user", partitionKey);

		HashMap<String, AttributeValue> valueMap = new HashMap<>();
		valueMap.put(":handle", new AttributeValue().withS(request.getOwner().getAlias()));

		QueryRequest queryRequest = new QueryRequest().withKeyConditionExpression("#user = :handle")
				.withTableName(tableName)
				.withExpressionAttributeNames(nameMap)
				.withExpressionAttributeValues(valueMap)
				.withScanIndexForward(false)
				.withLimit(10);

		if (request.getLastStatus() != null) {
			HashMap<String, AttributeValue> startKey = new HashMap<>();
			startKey.put(partitionKey, new AttributeValue().withS(request.getOwner().getAlias()));
			startKey.put(sortKey, new AttributeValue().withN(request.getLastStatus().getDate().toString()));
			queryRequest = queryRequest.withExclusiveStartKey(startKey);
		}

		try {
			StatusResponse response = new StatusResponse();
			QueryResult queryResult = amazonDynamoDB.query(queryRequest);
			List<Map<String, AttributeValue>> items = queryResult.getItems();
			List<Status> statuses = new ArrayList<>();
			if (items != null) {
				for (Map<String, AttributeValue> item : items) {
					statuses.add(gson.fromJson(item.get(STATUS_KEY).getS(), Status.class));
				}
			}
			response.setStatuses(statuses);
			Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
			if (lastKey != null) {
				response.setHasMorePages(true);
			}
			return response;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return new StatusResponse("Error getting feed.");
	}

	public void updateFeeds(UpdateFeedRequest request) {
		Gson gson = new Gson();
		List<Item> requests = new ArrayList<>();
		for (String alias : request.getAliases()) {
			requests.add(new Item()
					.withPrimaryKey(partitionKey, alias, sortKey, request.getStatus().getDate())
					.withString(STATUS_KEY, gson.toJson(request.getStatus())));
		}

		TableWriteItems tableWriteItems = new TableWriteItems(tableName).withItemsToPut(requests);
		BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);

		do {
			// ensure no items left behind #Obama
			Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();
			if (unprocessedItems.size() > 0) {
				outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
			}
		} while (outcome.getUnprocessedItems().size() > 0);
	}
}
