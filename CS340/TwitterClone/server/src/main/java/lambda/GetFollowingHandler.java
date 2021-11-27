package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.FollowingRequest;
import response.FollowingResponse;
import service.FollowingServiceImpl;

public class GetFollowingHandler implements RequestHandler<FollowingRequest, FollowingResponse> {

	/**
	 * Returns the users that the user specified in the request is following. Uses information in
	 * the request object to limit the number of followees returned and to return the next set of
	 * followees after any that were returned in a previous request.
	 *
	 * @param request contains the data required to fulfill the request.
	 * @param context the lambda context.
	 * @return the followees.
	 */
	@Override
	public FollowingResponse handleRequest(FollowingRequest request, Context context) {
		try {
			FollowingServiceImpl service = new FollowingServiceImpl();
			return service.getFollowees(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error");
		}
	}
}
