package edu.byu.cs.client.view.main;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import edu.byu.cs.client.R;
import edu.byu.cs.client.view.main.startup.StartupFragment;

public class StartupActivity extends AppCompatActivity implements StartupFragment.OnFragmentInteraction {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_startup);
		Iconify.with(new FontAwesomeModule());
		StartupFragment startup = new StartupFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.FragmentMount, startup)
				.commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		// check for authToken in future

	}

	@Override
	public void replaceFragment(Fragment f) {
		getSupportFragmentManager().beginTransaction()
				.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
				.replace(R.id.FragmentMount, f)
				.addToBackStack(null)
				.commit();
	}
}
