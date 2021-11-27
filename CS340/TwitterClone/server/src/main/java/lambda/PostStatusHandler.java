package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.PostRequest;
import response.PostResponse;
import service.PostServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PostStatusHandler implements RequestHandler<PostRequest, PostResponse> {
	@Override
	public PostResponse handleRequest(PostRequest request, Context context) {
		try {
			PostServiceImpl service = new PostServiceImpl();
			return service.postStatus(request);
		}
		catch (Exception e) {
//			throw new RuntimeException("Internal Error");
			return new PostResponse("Error posting status");
		}
	}
}
