package com.example.fmc.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.example.fmc.R;

public class MainFragment extends Fragment implements View.OnClickListener {
	private OnFragmentInteraction listener;

	public MainFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		v.findViewById(R.id.loginButton).setOnClickListener(this);
		v.findViewById(R.id.registerButton).setOnClickListener(this);
		return v;
	}

	public interface OnFragmentInteraction {
		void replaceFragment(Fragment f);
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
