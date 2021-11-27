package edu.byu.cs.client.view.main.startup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.byu.cs.client.R;
import edu.byu.cs.client.presenter.LoginPresenter;
import edu.byu.cs.client.view.asyncTasks.LoginTask;
import edu.byu.cs.client.view.main.MainActivity;
import request.LoginRequest;
import response.LoginResponse;

public class LoginFragment extends Fragment implements LoginPresenter.View {
	private EditText loginHandle;
	private EditText loginPassword;
	private Button loginButton;
	private LoginPresenter presenter;

	public LoginFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		presenter = new LoginPresenter(this);
		loginHandle = v.findViewById(R.id.loginHandle);
		loginPassword = v.findViewById(R.id.loginPassword);
		loginButton = v.findViewById(R.id.loginButton);
		loginButton.setEnabled(false);
		setListeners();
		return v;
	}

	private void setListeners() {
		TextWatcher tw = new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				validateForm();
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		};
		loginHandle.addTextChangedListener(tw);
		loginPassword.addTextChangedListener(tw);
		loginButton.setOnClickListener(v -> {
			new LoginTask(presenter).setListener(this::userRetrieved)
					.execute(new LoginRequest(loginHandle.getText().toString(), loginPassword.getText().toString()));
		});
	}

	private void validateForm() {
		if (!(loginHandle.getText().toString().equals("") || loginPassword.getText().toString().equals(""))) {
			loginButton.setEnabled(true);
		} else {
			loginButton.setEnabled(false);
		}
	}

	public void userRetrieved(LoginResponse response) {
		if (response.isSuccess()) {
			presenter.setCurrentUser(response.getUser());
			presenter.setAuthtoken(response.getAuthtoken());
			startActivity(new Intent(getActivity(), MainActivity.class));
			getActivity().finish();
		}
		else {
			Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
}
