package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.FollowRequest;
import response.FollowResponse;
import service.UpdateFollowServiceImpl;

public class UpdateFollowHandler implements RequestHandler<FollowRequest, FollowResponse> {
	@Override
	public FollowResponse handleRequest(FollowRequest request, Context context) {
		try {
			UpdateFollowServiceImpl service = new UpdateFollowServiceImpl();
			return service.updateFollow(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error");
		}
	}
}
