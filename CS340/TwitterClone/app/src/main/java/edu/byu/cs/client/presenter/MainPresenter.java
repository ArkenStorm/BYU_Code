package edu.byu.cs.client.presenter;

import edu.byu.cs.client.net.ServerFacade;
import request.LogoutRequest;
import response.LogoutResponse;

import java.io.IOException;

public class MainPresenter extends Presenter {

    private final View view;

    /**
     * The interface by which this presenter communicates with its view.
     */
    public interface View {
        // If needed, Specify methods here that will be called on the view in response to model updates
    }

    public MainPresenter(View view) {
        this.view = view;
    }

    public LogoutResponse logout(LogoutRequest request) throws IOException {
        return ServerFacade.getInstance().logout(request, "/logout");
    }
}
