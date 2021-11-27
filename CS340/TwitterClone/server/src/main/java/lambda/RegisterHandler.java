package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import request.RegisterRequest;
import response.LoginResponse;
import service.RegisterServiceImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterHandler implements RequestHandler<RegisterRequest, LoginResponse> {
	@Override
	public LoginResponse handleRequest(RegisterRequest request, Context context) {
		try {
			RegisterServiceImpl service = new RegisterServiceImpl();
			return service.handleRequest(request);
		}
		catch (Exception e) {
			throw new RuntimeException("Internal Error");
		}
	}
}
