package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import request.LogoutRequest;
import response.LogoutResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;

public class AuthTokenDAO {
	private static final String tableName = "authtoken";
	private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion("us-east-2")
			.build();
	private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
	private static final Table table = dynamoDB.getTable(tableName);
	private static final String partitionKey = "token";
	private static final String sortKey = "handle";
	private static final String KEY_DATE = "date";
	private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("-07:00");
	private static final long TIMEOUT = MILLISECONDS.convert(30, MINUTES);

	public static boolean isValidToken(String token, String handle) {
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(partitionKey, token, sortKey, handle);
		try {
			Item outcome = table.getItem(spec);
			if (outcome != null) {
				long timeOfToken = outcome.getLong(KEY_DATE);
				LocalDateTime dateOfToken = LocalDateTime.ofEpochSecond(timeOfToken, 0, ZONE_OFFSET);
				LocalDateTime fromTime = LocalDateTime.from(dateOfToken);
				long minutesElapsed = fromTime.until(LocalDateTime.now(), ChronoUnit.MINUTES);
				if (minutesElapsed >= TIMEOUT) {
					return false;
				}
				else {
					UpdateItemSpec updateSpec = new UpdateItemSpec().withPrimaryKey(partitionKey, token, sortKey, handle)
							.withUpdateExpression("set date = :a")
							.withValueMap(new ValueMap().withLong(":a", LocalDateTime.now().toInstant(ZONE_OFFSET).toEpochMilli()))
							.withReturnValues(ReturnValue.UPDATED_OLD);
					table.updateItem(updateSpec);
				}
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return true;
	}

	public static String createToken(String handle) {
		String token = UUID.randomUUID().toString();
		try {
			table.putItem(new Item().withString(partitionKey, token)
			.withString(sortKey, handle)
			.withLong(KEY_DATE, LocalDateTime.now().toInstant(ZONE_OFFSET).toEpochMilli()));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return token;
	}

	public LogoutResponse logout(LogoutRequest request) {
		try {
			if (isValidToken(request.getAuthToken(), request.getUser().getAlias())) {
				DeleteItemSpec spec = new DeleteItemSpec()
						.withPrimaryKey(partitionKey, request.getAuthToken(), sortKey, request.getUser().getAlias());
				table.deleteItem(spec);
				return new LogoutResponse(true);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return new LogoutResponse(false, "Error logging out.");
	}
}