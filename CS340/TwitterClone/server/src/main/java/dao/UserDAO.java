package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.KeysAndAttributes;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.Base64;
import com.google.gson.Gson;
import domain.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import request.*;
import response.GetUserResponse;
import response.LoginResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
	private static final String tableName = "users";
	private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.standard()
			.withRegion("us-east-2")
			.build();
	private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
	private static final Table table = dynamoDB.getTable(tableName);
	private static final String partitionKey = "handle";
	private static final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
			.withRegion("us-east-2")
			.build();
	private static final String S3_BUCKET = "taylorw1-profile-images";
	private static final String S3_IMAGE_URL = "https://taylorw1-profile-images.s3.us-east-2.amazonaws.com/";
	private static final String DEFAULT_IMAGE = "https://taylorw1-profile-images.s3.us-east-2.amazonaws.com/Demacia_profileicon.png";

	public LoginResponse registerUser(RegisterRequest request) {
		try {
			byte[] data = Base64.decode(request.getAvatar()); // Must be base 64 encoded!
			File imgFile = new File("/tmp/" + request.getHandle() + ".png");
			String imageURL;
			try (FileOutputStream os = new FileOutputStream(imgFile)) {
				os.write(data);
				CharSequence at = "@";
				CharSequence converted = "%40";
				String fileName = imgFile.getName().replace(at, converted);
				imageURL = S3_IMAGE_URL + fileName;
			}
			catch (Exception e) {
				imageURL = DEFAULT_IMAGE;
			}
			User newUser = new User(request.getFirstName(), request.getLastName(), imageURL);
			PutObjectRequest putObjectRequest = new PutObjectRequest(S3_BUCKET, imgFile.getName(), imgFile)
					.withCannedAcl(CannedAccessControlList.PublicRead);
			s3.putObject(putObjectRequest);

			String hashedPassword = BCrypt.hashpw(request.getPassword(), BCrypt.gensalt());
			table.putItem(new Item().withString(partitionKey, request.getHandle())
					.withString("firstName", request.getFirstName())
			.withString("lastName", request.getLastName())
			.withString("email", request.getEmail())
			.withString("password", hashedPassword)
			.withString("imageURL", imageURL));

			return new LoginResponse(true, newUser);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return new LoginResponse(false, "Error in registration.");
	}

	public LoginResponse loginUser(LoginRequest request) {
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(partitionKey, request.getHandle());
		try {
			User user = getUser(spec);
			return new LoginResponse(true, user);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return new LoginResponse(false, "Failed to login.");
	}

	public GetUserResponse findUserByHandle(GetUserRequest request) {
		GetItemSpec spec = new GetItemSpec().withPrimaryKey(partitionKey, request.getHandle());
		try {
			User user = getUser(spec);
			return new GetUserResponse(user, false);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return new GetUserResponse("Error retrieving user.");
	}

	public List<User> findListOfUsersByHandle(List<String> handles) {
		List<User> response = new ArrayList<>();

		TableKeysAndAttributes tableKeysAndAttributes = new TableKeysAndAttributes(tableName);
		for (String handle : handles) {
			tableKeysAndAttributes.addHashOnlyPrimaryKey(partitionKey, handle);
		}

		BatchGetItemOutcome outcome = dynamoDB.batchGetItem(tableKeysAndAttributes);
		Map<String, KeysAndAttributes> unprocessed;
		do {
			List<Item> items = outcome.getTableItems().get(tableName);
			for (Item item : items) {
				response.add(new User(item.getString("firstName"), item.getString("lastName"),
						item.getString(partitionKey), item.getString("imageURL")));
			}
			// no user left behind #Obama
			unprocessed = outcome.getUnprocessedKeys();
			if (!unprocessed.isEmpty()) {
				outcome = dynamoDB.batchGetItemUnprocessed(unprocessed);
			}
		} while (!unprocessed.isEmpty());
		response.sort(Comparator.comparing(User::getAlias));

		return response;
	}

	private User getUser(GetItemSpec spec) {
		Item outcome = table.getItem(spec);
		User user = new User();
		user.setAlias(outcome.getString(partitionKey));
		user.setFirstName(outcome.getString("firstName"));
		user.setLastName(outcome.getString("lastName"));
		user.setImageUrl(outcome.getString("imageURL"));
		return user;
	}
}
