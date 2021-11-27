package edu.byu.cs.client.presenter;

import edu.byu.cs.client.model.service.PostServiceProxy;
import request.PostRequest;
import response.PostResponse;

import java.io.IOException;

public class PostPresenter {
    private final View view;

    public interface View {

    }

    public PostPresenter(View view) {
        this.view = view;
    }

    public PostResponse postStatus(PostRequest request) throws IOException {
        return PostServiceProxy.getInstance().postStatus(request);
    }
}
