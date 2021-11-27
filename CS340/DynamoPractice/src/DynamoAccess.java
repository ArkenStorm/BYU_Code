import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;

import javax.management.Query;
import java.util.HashMap;
import java.util.Iterator;

public class DynamoAccess {
    public static Table table;

    public static void main(String[] args) throws Exception {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_EAST_2)
                .build();

        DynamoDB dynamoDB = new DynamoDB(client);

        table = dynamoDB.getTable("follows");
        //makeTable();
        Item item = getItem();
        updateItem(item);
        addItem();
        deleteItem();
        queryTable();
        queryIndex();
    }

    public static void makeTable() {
        try {
            System.out.println("Adding 100 new items...");
            for (int i = 0; i < 100; i++) {
                table.putItem(new Item().withString("follower_handle", "@Joe" + i)
                        .withString("follower_name", "Joe" + i)
                        .withString("followee_handle", "@Bob" + i)
                        .withString("followee_name", "Bob" + i));
            }
            System.out.println("PutItem succeeded.");

        }
        catch (Exception e) {
            System.err.println("Unable to add item");
            System.err.println(e.getMessage());
        }
    }

    public static void addItem() {
        try {
            table.putItem(new Item().withString("follower_handle", "@taylorw1")
                    .withString("follower_name", "Taylor W")
                    .withString("followee_handle", "@MkII")
                    .withString("followee_name", "Michael II"));
        }
        catch (Exception e) {
            System.err.println("Unable to add item");
            System.err.println(e.getMessage());
        }
    }

    public static Item getItem() {
        GetItemSpec spec = new GetItemSpec().withPrimaryKey("follower_handle", "@Joe22", "followee_handle", "@Bob22");

        try {
            System.out.println("Attempting to read item...");
            Item outcome = table.getItem(spec);
            System.out.println("Succeeded: " + outcome);
            return outcome;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void updateItem(Item item) {
        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("follower_handle", "@Joe22", "followee_handle", "@Bob22")
                .withUpdateExpression("set follower_name = :a, followee_name = :b")
                .withValueMap(new ValueMap().withString(":a", "Joe Hills").withString(":b", "Bob Parr"))
                .withReturnValues(ReturnValue.UPDATED_NEW);


        try {
            System.out.println("Updating the item...");
            UpdateItemOutcome outcome = table.updateItem(updateItemSpec);
            System.out.println("UpdateItem succeeded:\n" + outcome.getItem().toJSONPretty());
        }
        catch (Exception e) {
            System.err.println("Unable to update item");
            System.err.println(e.getMessage());
        }
    }

    public static void deleteItem() {
        DeleteItemSpec deleteItemSpec = new DeleteItemSpec()
                .withPrimaryKey(new PrimaryKey("follower_handle", "@taylorw1", "followee_handle", "@MkII"));

        try {
            System.out.println("Attempting a delete...");
            table.deleteItem(deleteItemSpec);
            System.out.println("DeleteItem succeeded");
        }
        catch (Exception e) {
            System.err.println("Unable to delete item");
            System.err.println(e.getMessage());
        }
    }

    public static void queryTable() {
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#follower", "follower_handle");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":handle", "@Joe22");

        QuerySpec querySpec = new QuerySpec().withKeyConditionExpression("#follower = :handle").withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(true);

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            System.out.println("Followees:");
            items = table.query(querySpec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.getString("followee_handle"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void queryIndex() {
        HashMap<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#followee", "followee_handle");

        HashMap<String, Object> valueMap = new HashMap<String, Object>();
        valueMap.put(":handle", "@Bob22");


        Index tableIndex = table.getIndex("follows_index");
        QuerySpec spec = new QuerySpec().withKeyConditionExpression("#followee = :handle").withNameMap(nameMap)
                .withValueMap(valueMap)
                .withScanIndexForward(false);

        ItemCollection<QueryOutcome> items = null;
        Iterator<Item> iterator = null;
        Item item = null;

        try {
            System.out.println("Followers:");
            items = tableIndex.query(spec);

            iterator = items.iterator();
            while (iterator.hasNext()) {
                item = iterator.next();
                System.out.println(item.getString("follower_handle"));
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
