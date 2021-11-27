package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.StatusRequest;
import response.StatusResponse;
import service.FeedServiceImpl;

public class GetFeedHandler implements RequestHandler<StatusRequest, StatusResponse> {
	@Override
	public StatusResponse handleRequest(StatusRequest request, Context context) {
		try {
			FeedServiceImpl service = new FeedServiceImpl();
			return service.getFeed(request);
		}
		catch (Exception e) {
			return new StatusResponse("Error retrieving feed.");
			//throw new RuntimeException("Internal Error." + e.getMessage()); // will this catch the other runtime exception?
		}
	}
}
