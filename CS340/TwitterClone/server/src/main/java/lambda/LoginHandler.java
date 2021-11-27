package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.LoginRequest;
import response.LoginResponse;
import service.LoginServiceImpl;

public class LoginHandler implements RequestHandler<LoginRequest, LoginResponse> {
	@Override
	public LoginResponse handleRequest(LoginRequest request, Context context) {
		try {
			LoginServiceImpl service = new LoginServiceImpl();
			return service.login(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error");
		}
	}
}
