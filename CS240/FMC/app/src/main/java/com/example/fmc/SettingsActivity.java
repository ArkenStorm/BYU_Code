package com.example.fmc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.example.fmc.fragments.MainFragment;

public class SettingsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		getSupportFragmentManager().beginTransaction()
			.replace(R.id.settingsFragment, new SettingsFragment())
			.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
		return true;
	}

	public static class SettingsFragment extends PreferenceFragmentCompat {
		SharedPreferences.OnSharedPreferenceChangeListener prefChanged = (sharedPreferences, key) -> {
			Datacache.getInstance().setSettingsUpdated(true);
		};

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getPreferenceManager().getDefaultSharedPreferences(getActivity())
					.registerOnSharedPreferenceChangeListener(prefChanged);
		}

		@Override
		public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
			setPreferencesFromResource(R.xml.fragment_settings, rootKey);
			Preference logout = findPreference("logout");
			logout.setOnPreferenceClickListener(p -> {
				Datacache.getInstance().setLoggedIn(false);
				Intent intent = new Intent(getActivity(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			});
		}
	}
}
