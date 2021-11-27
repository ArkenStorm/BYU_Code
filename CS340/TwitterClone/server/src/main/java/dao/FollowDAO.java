package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.QueryRequest;
import com.amazonaws.services.dynamodbv2.model.QueryResult;
import com.amazonaws.services.dynamodbv2.xspec.S;
import internal.AliasResponse;
import request.FollowRequest;
import request.FollowersRequest;
import request.FollowingRequest;
import request.GetUserRequest;
import response.FollowResponse;

import java.util.*;

public class FollowDAO {
	private static final String tableName = "follows";
	private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion("us-east-2")
			.build();
	private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
	private static final Table table = dynamoDB.getTable(tableName);
	private static final String partitionKey = "follower_handle";
	private static final String sortKey = "followee_handle";
	private static final String index = "follows_index";

	public FollowResponse addFollow(FollowRequest request) {
		try {
			table.putItem(new Item()
					.withPrimaryKey(partitionKey, request.getCurrentUser().getAlias(), sortKey, request.getUser().getAlias()));
			return new FollowResponse(true);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new FollowResponse("Error following " + request.getUser().getAlias() + ".");
	}

	public FollowResponse deleteFollow(FollowRequest request) {
		DeleteItemSpec spec = new DeleteItemSpec()
				.withPrimaryKey(partitionKey, request.getCurrentUser().getAlias(), sortKey, request.getUser().getAlias());
		try {
			table.deleteItem(spec);
			return new FollowResponse(false);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new FollowResponse("Error unfollowing " + request.getUser().getAlias() + ".");
	}

	public AliasResponse getFollowees(FollowingRequest request) {
		HashMap<String, String> nameMap = new HashMap<>();
		nameMap.put("#follower", partitionKey);

		HashMap<String, AttributeValue> valueMap = new HashMap<>();
		valueMap.put(":handle", new AttributeValue().withS(request.getFollower().getAlias()));

		QueryRequest queryRequest = new QueryRequest().withKeyConditionExpression("#follower = :handle")
				.withTableName(tableName)
				.withExpressionAttributeNames(nameMap)
				.withExpressionAttributeValues(valueMap)
				.withScanIndexForward(true)
				.withLimit(10);

		if (request.getLastFollowee() != null) {
			HashMap<String, AttributeValue> startKey = new HashMap<>();
			startKey.put(partitionKey, new AttributeValue().withS(request.getFollower().getAlias()));
			startKey.put(sortKey, new AttributeValue().withS(request.getLastFollowee().getAlias()));
			queryRequest = queryRequest.withExclusiveStartKey(startKey);
		}

		try {
			AliasResponse response = new AliasResponse();
			QueryResult queryResult = amazonDynamoDB.query(queryRequest);
			List<Map<String, AttributeValue>> items = queryResult.getItems();
			List<String> aliases = new ArrayList<>();
			if(items != null) {
				for(Map<String , AttributeValue> item : items){
					aliases.add(item.get(partitionKey).getS());
				}
				response.setAliases(aliases);
			}
			Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
			if(lastKey != null){
				response.setHasMorePages(true);
			}
			return response;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return new AliasResponse(null, false);
	}

	public AliasResponse getFollowers(FollowersRequest request) {
		HashMap<String, String> nameMap = new HashMap<>();
		nameMap.put("#follower", sortKey);

		HashMap<String, AttributeValue> valueMap = new HashMap<>();
		valueMap.put(":handle", new AttributeValue().withS(request.getFollowee().getAlias()));

		QueryRequest queryRequest = new QueryRequest().withKeyConditionExpression("#follower = :handle")
				.withTableName(tableName)
				.withIndexName(index)
				.withExpressionAttributeNames(nameMap)
				.withExpressionAttributeValues(valueMap)
				.withScanIndexForward(true)
				.withLimit(10);

		if (request.getLastFollower() != null) {
			HashMap<String, AttributeValue> startKey = new HashMap<>();
			startKey.put(sortKey, new AttributeValue().withS(request.getFollowee().getAlias()));
			startKey.put(partitionKey, new AttributeValue().withS(request.getLastFollower().getAlias()));
			queryRequest = queryRequest.withExclusiveStartKey(startKey);
		}

		try {
			AliasResponse response = new AliasResponse();
			QueryResult queryResult = amazonDynamoDB.query(queryRequest);
			List<Map<String, AttributeValue>> items = queryResult.getItems();
			List<String> aliases = new ArrayList<>();
			if(items != null) {
				for(Map<String , AttributeValue> item : items){
					aliases.add(item.get(sortKey).getS());
				}
				response.setAliases(aliases);
			}
			Map<String, AttributeValue> lastKey = queryResult.getLastEvaluatedKey();
			if(lastKey != null){
				response.setHasMorePages(true);
			}
			return response;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return new AliasResponse(null, false);
	}

	public boolean checkFollow(GetUserRequest request) {
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(partitionKey, request.getCurrentUser().getAlias(), sortKey, request.getHandle());
		try {
			table.getItem(spec);
			return true;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return false;
	}
}
