package com.example.fmc.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.fmc.*;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import request.LoginRequest;
import result.AuthorizationResult;
import result.MessageResult;
import result.Result;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

public class LoginFragment extends Fragment {
	private EditText loginUsername;
	private EditText loginPassword;
	private Button loginButton;
	private EditText host;
	private EditText port;
	private String serverURL;

	public LoginFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		loginUsername = v.findViewById(R.id.loginUsername);
		loginPassword = v.findViewById(R.id.loginPassword);
		loginButton = v.findViewById(R.id.triggerLogin);
		loginButton.setEnabled(false);
		this.setListeners();
		return v;
	}

	private void validateForm() {
		if (!(loginUsername.getText().toString().equals("") || loginPassword.getText().toString().equals(""))) {
			loginButton.setEnabled(true);
		} else {
			loginButton.setEnabled(false);
		}
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
		loginUsername.addTextChangedListener(tw);
		loginPassword.addTextChangedListener(tw);
		host = getActivity().findViewById(R.id.serverHost);
		port = getActivity().findViewById(R.id.serverPort);
		loginButton.setOnClickListener(v -> {
			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getRootView().getWindowToken(), 0);
			getActivity().findViewById(R.id.serverSettings).setVisibility(View.GONE);
			serverURL = "http://" + host.getText().toString() + ":" + port.getText().toString() + "/user/login";
			new LoginTask().setListener(result -> ((MainActivity)getActivity()).replaceFragment(new MapFragment()))
					.execute(new LoginRequest(loginUsername.getText().toString(), loginPassword.getText().toString()));
		});
	}

	public class LoginTask extends AsyncTask<LoginRequest, Void, Result> {
		private Consumer<AuthorizationResult> listener;

		@Override
		protected Result doInBackground(LoginRequest... requests) {
			try {
				return ServerProxy.authorizeUser(requests[0], new URL(serverURL));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Result result) {
			if (result instanceof AuthorizationResult) {
				Datacache cache = Datacache.getInstance();
				cache.setToken(((AuthorizationResult) result).getAuthToken());
				cache.setUserPersonID(((AuthorizationResult) result).getPersonID());
				cache.setLoggedIn(true);
				serverURL = "http://" + host.getText().toString() + ":" + port.getText().toString() + "/person";
				try {
					new Utility.PersonTask(new URL(serverURL), getActivity()).execute();
					serverURL = "http://" + host.getText().toString() + ":" + port.getText().toString() + "/event";
					new Utility.EventTask(new URL(serverURL), getActivity()).execute();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				listener.accept((AuthorizationResult) result);
			}
			else {
				Toast.makeText(getActivity(), ((MessageResult)result).getMessage(), Toast.LENGTH_LONG).show();
			}
		}

		public LoginTask setListener(Consumer<AuthorizationResult> listener) {
			this.listener = listener;
			return this;
		}
	}
}
