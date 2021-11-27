package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import dao.FollowDAO;
import domain.Status;
import domain.User;
import internal.AliasResponse;
import internal.UpdateFeedRequest;
import request.FollowersRequest;
import request.PostRequest;

public class PostUpdateFeedMessagesHandler implements RequestHandler<SQSEvent, Void> {
	private static final String PostUpdateFeedMessagesQueue = "https://sqs.us-east-2.amazonaws.com/596104686138/PostStatus";
	private static final String UpdateFeedQueueURL = "https://sqs.us-east-2.amazonaws.com/596104686138/UpdateFeed";

	@Override
	public Void handleRequest(SQSEvent input, Context context) {
		Gson gson = new Gson();
		AmazonSQS queue = AmazonSQSClientBuilder.defaultClient();
		for (SQSEvent.SQSMessage message : input.getRecords()) {
			queue.deleteMessage(PostUpdateFeedMessagesQueue, message.getReceiptHandle());
		}
		for (SQSEvent.SQSMessage message : input.getRecords()) {
			PostRequest request = gson.fromJson(message.getBody(), PostRequest.class);
			FollowDAO dao = new FollowDAO();
			User lastFollower = null;
			AliasResponse response;
			do {
				response = dao.getFollowers(new FollowersRequest(request.getOwner(), 10, lastFollower));
				lastFollower = new User(null, null, response.getAliases().get(response.getAliases().size() - 1), null);
				UpdateFeedRequest updateFeedRequest = new UpdateFeedRequest(response.getAliases(), new Status(request.getOwner(), request.getStatusText()));
				SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(UpdateFeedQueueURL)
						.withMessageBody(gson.toJson(updateFeedRequest));
				queue.sendMessage(sendMessageRequest);
			} while (response.isHasMorePages());
		}
		return null;
	}
}
