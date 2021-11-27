package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.StatusRequest;
import response.StatusResponse;
import service.StoryServiceImpl;

public class GetStoryHandler implements RequestHandler<StatusRequest, StatusResponse> {
	@Override
	public StatusResponse handleRequest(StatusRequest request, Context context) {
		try {
			StoryServiceImpl service = new StoryServiceImpl();
			return service.getStory(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error. " + e.getMessage());
		}
	}
}
