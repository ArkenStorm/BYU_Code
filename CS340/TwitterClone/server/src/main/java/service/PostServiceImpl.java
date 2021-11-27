package service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.google.gson.Gson;
import dao.AuthTokenDAO;
import dao.StoryDAO;
import request.PostRequest;
import response.PostResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PostServiceImpl implements PostService {
	private static final String queueURL = "https://sqs.us-east-2.amazonaws.com/596104686138/PostStatus";

	@Override
	public PostResponse postStatus(PostRequest request) {
		if (!AuthTokenDAO.isValidToken(request.getAuthToken(), request.getOwner().getAlias())) {
			throw new RuntimeException("Forbidden");
		}
		StoryDAO dao = new StoryDAO();
		PostResponse response = dao.postStatus(request);
		if (response.isSuccess()) {
			Gson gson = new Gson();
			AmazonSQS queue = AmazonSQSClientBuilder.defaultClient();
			SendMessageRequest sendMessageRequest = new SendMessageRequest().withQueueUrl(queueURL)
					.withMessageBody(gson.toJson(request)).withDelaySeconds(5);
			queue.sendMessage(sendMessageRequest);
		}
		return response;
	}
}
