package pro.burgerz.wsm.mods.holidaysmodule;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

public class Settings extends Activity {
	public static String PREF_KEY_UPDATE_SERVER = "holidays_update_server";
	static Context context;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        context = Settings.this;
        if (savedInstanceState == null)
			getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new PrefsFragment()).commit();
	}

	public static class PrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
			addPreferencesFromResource(R.xml.preferences);
			SharedPreferences sharedPref = getPreferenceScreen().getSharedPreferences();
			sharedPref.registerOnSharedPreferenceChangeListener(this);
			onSharedPreferenceChanged(sharedPref, PREF_KEY_UPDATE_SERVER);
			
			
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences pref, String key) {
			if (key.equals(PREF_KEY_UPDATE_SERVER)) {
				ListPreference list = (ListPreference) findPreference(PREF_KEY_UPDATE_SERVER);
				list.setSummary(list.getEntry());
			}
		}

		
	}
	
}
