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
import android.widget.*;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import com.example.fmc.*;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import request.RegisterRequest;
import result.AuthorizationResult;
import result.MessageResult;
import result.Result;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.from;

public class RegisterFragment extends Fragment {
	private EditText registerUsername;
	private EditText registerPassword;
	private EditText registerFirstName;
	private EditText registerLastName;
	private EditText registerEmail;
	private RadioGroup registerGenderGroup;
	private RadioButton registerGender;
	private Button registerButton;
	private EditText host;
	private EditText port;
	private String serverURL;

	public RegisterFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_register, container, false);
		registerUsername = v.findViewById(R.id.registerUsername);
		registerPassword = v.findViewById(R.id.registerPassword);
		registerFirstName = v.findViewById(R.id.registerFirstName);
		registerLastName = v.findViewById(R.id.registerLastName);
		registerEmail = v.findViewById(R.id.registerEmail);
		registerGenderGroup = v.findViewById(R.id.registerGenderGroup);
		registerGender = v.findViewById(registerGenderGroup.getCheckedRadioButtonId());
		registerButton = v.findViewById(R.id.triggerRegister);
		registerButton.setEnabled(false);
		this.setListeners();
		return v;
	}

	private void validateForm() {
		if (!(registerUsername.getText().toString().equals("") || registerPassword.getText().toString().equals("")
				|| registerFirstName.getText().toString().equals("") || registerLastName.getText().toString().equals("")
				|| registerEmail.getText().toString().equals("") || registerGender.getText().equals(""))) {
			registerButton.setEnabled(true);
		} else {
			registerButton.setEnabled(false);
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
		registerUsername.addTextChangedListener(tw);
		registerPassword.addTextChangedListener(tw);
		registerFirstName.addTextChangedListener(tw);
		registerLastName.addTextChangedListener(tw);
		registerEmail.addTextChangedListener(tw);
		registerGenderGroup.setOnCheckedChangeListener((group, checkedId) -> registerGender = group.findViewById(checkedId));
		host = getActivity().findViewById(R.id.serverHost);
		port = getActivity().findViewById(R.id.serverPort);
		registerButton.setOnClickListener(v -> {
			InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(v.getRootView().getWindowToken(), 0);
			getActivity().findViewById(R.id.serverSettings).setVisibility(View.GONE);
			serverURL = "http://" + host.getText().toString() + ":" + port.getText().toString() + "/user/register";
			String gender = registerGender.getId() == R.id.registerMale ? "m" : "f";
			new RegisterTask().setListener(result -> ((MainActivity)getActivity()).replaceFragment(new MapFragment()))
					.execute(new RegisterRequest(registerUsername.getText().toString(), registerPassword.getText().toString(),
					registerEmail.getText().toString(), registerFirstName.getText().toString(), registerLastName.getText().toString(), gender));
		});
	}

	public class RegisterTask extends AsyncTask<RegisterRequest, Void, Result> {
		private Consumer<AuthorizationResult> listener;

		@Override
		protected Result doInBackground(RegisterRequest... requests) {
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

		public RegisterFragment.RegisterTask setListener(Consumer<AuthorizationResult> listener) {
			this.listener = listener;
			return this;
		}
	}
}
