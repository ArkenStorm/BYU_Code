package com.example.fmc;

import android.content.Intent;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.fmc.fragments.MainFragment;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteraction {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			MainFragment main = new MainFragment();
			getSupportFragmentManager().beginTransaction()
					.add(R.id.FragmentMount, main)
					.commit();
		}
		Iconify.with(new FontAwesomeModule());
	}

	@Override
	public void onResume() {
		super.onResume();
		if (!Datacache.getInstance().isLoggedIn()) {
			MainFragment main = new MainFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.FragmentMount, main)
					.commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.mapMenuSearch) {
			startActivity(new Intent(this, SearchActivity.class));
		}
		else if (item.getItemId() == R.id.mapMenuSettings) {
			startActivity(new Intent(this, SettingsActivity.class));
		}
		return true;
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
