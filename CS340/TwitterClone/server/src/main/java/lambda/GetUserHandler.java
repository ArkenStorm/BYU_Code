package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import dao.UserDAO;
import request.GetUserRequest;
import response.GetUserResponse;
import service.GetUserServiceImpl;

public class GetUserHandler implements RequestHandler<GetUserRequest, GetUserResponse> {
	@Override
	public GetUserResponse handleRequest(GetUserRequest request, Context context) {
		try {
			GetUserServiceImpl service = new GetUserServiceImpl();
			return service.findUserByHandle(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error");
		}
	}
}
