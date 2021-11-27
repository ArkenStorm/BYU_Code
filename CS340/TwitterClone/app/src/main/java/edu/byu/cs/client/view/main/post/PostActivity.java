package edu.byu.cs.client.view.main.post;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import edu.byu.cs.client.R;
import edu.byu.cs.client.model.service.LoginServiceProxy;
import edu.byu.cs.client.presenter.PostPresenter;
import edu.byu.cs.client.view.asyncTasks.PostTask;
import request.PostRequest;
import response.PostResponse;

public class PostActivity extends AppCompatActivity implements PostPresenter.View, PostTask.PostTaskObserver {
	private PostPresenter presenter;
	private EditText editStatus;
	private Button cancelButton;
	private Button postButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);

		presenter = new PostPresenter(this);
		editStatus = findViewById(R.id.editStatus);
		cancelButton = findViewById(R.id.cancelPost);
		postButton = findViewById(R.id.postStatus);
		postButton.setEnabled(false);

		TextWatcher tw = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				checkTextLength(s.length());
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		};
		editStatus.addTextChangedListener(tw);

		postButton.setOnClickListener(v -> {
			new PostTask(presenter, this)
					.execute(new PostRequest(LoginServiceProxy.getInstance().getCurrentUser(), editStatus.getText().toString()));
		});

		cancelButton.setOnClickListener(v -> finish());
	}

	private void checkTextLength(int textLength) {
		if (textLength > 0 && textLength <= 256) {
			postButton.setEnabled(true);
		}
		else {
			postButton.setEnabled(false);
		}
	}

	@Override
	public void postResult(PostResponse postResponse) {
		if (postResponse.isSuccess()) {
			this.finish();
		}
		else {
			Toast.makeText(this, postResponse.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
