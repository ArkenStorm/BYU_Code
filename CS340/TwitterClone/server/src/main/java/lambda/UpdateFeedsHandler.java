package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.google.gson.Gson;
import dao.FeedDAO;
import internal.UpdateFeedRequest;

public class UpdateFeedsHandler implements RequestHandler<SQSEvent, Void> {
	private static final String queueURL = "https://sqs.us-east-2.amazonaws.com/596104686138/UpdateFeed";

	@Override
	public Void handleRequest(SQSEvent input, Context context) {
		Gson gson = new Gson();
		AmazonSQS queue = AmazonSQSClientBuilder.defaultClient();
		for (SQSEvent.SQSMessage message : input.getRecords()) {
			queue.deleteMessage(queueURL, message.getReceiptHandle());
		}
		for (SQSEvent.SQSMessage message : input.getRecords()) {
			UpdateFeedRequest request = gson.fromJson(message.getBody(), UpdateFeedRequest.class);
			FeedDAO dao = new FeedDAO();
			dao.updateFeeds(request);
		}
		return null;
	}
}
