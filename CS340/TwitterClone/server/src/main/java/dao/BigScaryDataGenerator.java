package dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;
import com.google.gson.Gson;

import domain.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
import request.RegisterRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BigScaryDataGenerator {
    private static final String UserTableName = "users";
    private static final String AliasAttr = "handle";

    private static final String FollowTableName = "follows";
    private static final String FollowerAttr = "follower_handle";
    private static final String FolloweeAttr = "followee_handle";

    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
                                                        .standard()
                                                        .withRegion("us-east-2")
                                                        .build();
    private static DynamoDB dynamoDB = new DynamoDB(amazonDynamoDB);
    private static Logger logger = Logger.getLogger("BigScaryDataGenerator");

    private static final String MALE_NAMES_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/json/mnames.json";
    private static final String FEMALE_NAMES_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/json/fnames.json";
    private static final String SURNAMES_URL = "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/json/snames.json";

    private static final String [] maleNames;
    private static final String [] femaleNames;
    private static final String [] surnames;

    private static final String MALE_IMAGE_URL = "https://taylorw1-profile-images.s3.us-east-2.amazonaws.com/Demacia_profileicon.png";
    private static final String FEMALE_IMAGE_URL = "https://taylorw1-profile-images.s3.us-east-2.amazonaws.com/Demacia_profileicon.png";

    static {
        try {
            maleNames = loadNamesFromJSon(MALE_NAMES_URL);
            femaleNames = loadNamesFromJSon(FEMALE_NAMES_URL);
            surnames = loadNamesFromJSon(SURNAMES_URL);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void GenerateUsersAndFollows(){
        Gson gson = new Gson();
        int counter = 0;
        int aliasOffset = 1;
        int sendOffset;
        User mainUser = new User("Test", "User", MALE_IMAGE_URL);
        RegisterRequest registerRequest = new RegisterRequest("email", "password", "Test", "User", "@TestUser", MALE_IMAGE_URL);
        UserDAO userDAO = new UserDAO();
        userDAO.registerUser(registerRequest);

        List<User> users = new ArrayList<>();
        users.add(mainUser);

        while(counter <= 10000){

            for(int i = 0; i < maleNames.length && i < surnames.length; i++, counter++){
                users.add(new User(maleNames[i], surnames[i],"@" + maleNames[i] + surnames[i] + aliasOffset, MALE_IMAGE_URL));
            }

            for(int i = 0; i < femaleNames.length && i < surnames.length; i++, counter++){
                users.add(new User(femaleNames[i], surnames[i],"@" + femaleNames[i] + surnames[i] + aliasOffset, FEMALE_IMAGE_URL));
            }

            sendOffset = 0;
            //add users to User table
            while (sendOffset < users.size()) {

                List<Item> requests = new ArrayList<>();
                for (int i = 0; i < 25 && i + sendOffset < users.size(); i++) {
                    Item item = new Item()
                            .withPrimaryKey(AliasAttr, users.get(i + sendOffset).getAlias())
                            .withString("firstName", users.get(i + sendOffset).getFirstName())
                            .withString("lastName", users.get(i + sendOffset).getLastName())
                            .withString("email", "email")
                            .withString("password", "password")
                            .withString("imageURL", MALE_IMAGE_URL);

                    requests.add(item);
                }
                TableWriteItems tableWriteItems = new TableWriteItems(UserTableName)
                        .withItemsToPut(requests);

                BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);

                do {

                    // Check for unprocessed keys which could happen if you exceed
                    // provisioned throughput

                    Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

                    if (outcome.getUnprocessedItems().size() == 0) {
                        logger.log(Level.INFO, "No unprocessed items found");
                    }
                    else {
                        logger.log(Level.INFO, "Retrieving the unprocessed items");
                        outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
                    }

                } while (outcome.getUnprocessedItems().size() > 0);
                sendOffset += 25;
            }

            sendOffset = 0;
            //make everyone follow the main user
            while (sendOffset < users.size()){

                List<Item> requests = new ArrayList<>();
                for (int i = 0; i < 25 && i + sendOffset < users.size(); i++) {
                    Item item = new Item()
                            .withPrimaryKey(FollowerAttr, users.get(i + sendOffset).getAlias())
                            .withString(FolloweeAttr, mainUser.getAlias());

                    requests.add(item);
                }
                TableWriteItems tableWriteItems = new TableWriteItems(FollowTableName)
                        .withItemsToPut(requests);

                BatchWriteItemOutcome outcome = dynamoDB.batchWriteItem(tableWriteItems);

                do {

                    // Check for unprocessed keys which could happen if you exceed
                    // provisioned throughput

                    Map<String, List<WriteRequest>> unprocessedItems = outcome.getUnprocessedItems();

                    if (outcome.getUnprocessedItems().size() == 0) {
                        logger.log(Level.INFO, "No unprocessed items found");
                    }
                    else {
                        logger.log(Level.INFO, "Retrieving the unprocessed items");
                        outcome = dynamoDB.batchWriteItemUnprocessed(unprocessedItems);
                    }

                } while (outcome.getUnprocessedItems().size() > 0);
                sendOffset += 25;
            }

            users.clear();
            aliasOffset++;
        }
    }

    public void updatePeeps(){
        Table table = dynamoDB.getTable("LoginInfo");

        table.updateItem("alias", "@TestUser", new AttributeUpdate("password").put(BCrypt.hashpw("asdf", BCrypt.gensalt())));
    }

    private static String [] loadNamesFromJSon(String urlString) throws IOException {

        Names names;

        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                names = (new Gson()).fromJson(br, Names.class);

            } else {
                throw new IOException("Unable to read from url. Response code: " + connection.getResponseCode());
            }

            connection.disconnect();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return names == null ? null : names.getNames();
    }

    private class Names {

        @SuppressWarnings("unused")
        private String [] data;

        private String [] getNames() {
            return data;
        }
    }
}
