package pro.burgerz.wsm.mods.searchplaystore;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;

public class SettingsActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
			getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new PrefsFragment()).commit();
	}

	public static class PrefsFragment extends PreferenceFragment
			implements SharedPreferences.OnSharedPreferenceChangeListener {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
			addPreferencesFromResource(R.xml.preferences);
			SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
			sharedPref.registerOnSharedPreferenceChangeListener(this);
			onSharedPreferenceChanged(sharedPref, "use_google_market");
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals("use_google_market")) {
				CheckBoxPreference pref = (CheckBoxPreference) findPreference(key);
				boolean value = sharedPreferences.getBoolean(key, true);
				if (!value)
					pref.setSummary(R.string.xiaomi);
				else pref.setSummary(R.string.google);
			}
		}
	}
}
