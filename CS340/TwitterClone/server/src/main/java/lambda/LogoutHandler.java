package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.LogoutRequest;
import response.LogoutResponse;
import service.LoginServiceImpl;

public class LogoutHandler implements RequestHandler<LogoutRequest, LogoutResponse> {
	@Override
	public LogoutResponse handleRequest(LogoutRequest request, Context context) {
		try {
			LoginServiceImpl service = new LoginServiceImpl();
			return service.logout(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error");
		}
	}
}
