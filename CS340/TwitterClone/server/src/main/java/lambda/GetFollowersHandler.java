package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.FollowersRequest;
import response.FollowersResponse;
import service.FollowersServiceImpl;

public class GetFollowersHandler implements RequestHandler<FollowersRequest, FollowersResponse> {
	@Override
	public FollowersResponse handleRequest(FollowersRequest request, Context context) {
		try {
			FollowersServiceImpl service = new FollowersServiceImpl();
			return service.getFollowers(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error");
		}
	}
}
