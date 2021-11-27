package edu.byu.cs.client.view.main.startup;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.byu.cs.client.R;


public class StartupFragment extends Fragment implements View.OnClickListener {
	private OnFragmentInteraction listener;

	public StartupFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_startup, container, false);
		v.findViewById(R.id.loginButton).setOnClickListener(this);
		v.findViewById(R.id.registerButton).setOnClickListener(this);
		return v;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteraction) {
			listener = (OnFragmentInteraction) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	public interface OnFragmentInteraction {
		void replaceFragment(Fragment f);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.loginButton:
				listener.replaceFragment(new LoginFragment());
				break;
			case R.id.registerButton:
				listener.replaceFragment(new RegisterFragment());
				break;
		}
	}
}
