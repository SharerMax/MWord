package net.sharermax.mword;

import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.text.StaticLayout;

public class SettingFragment extends PreferenceFragment {

	private ListPreference remGestureToRight;
	private ListPreference remGestureToLeft;
	private ListPreference remGestureToUp;
	private ListPreference remGestureToDown;
	private final static String TO_RIGHT_KEY = "gestureToRight";
	private final static String TO_LEFT_KEY = "gestureToLeft";
	private final static String TO_UP_KEY = "gestureToUp";
	private final static String TO_DOWN = "gestureToDown";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.setting_fragment);
		remGestureToRight = (ListPreference)findPreference(TO_RIGHT_KEY);
		remGestureToLeft = (ListPreference)findPreference(TO_LEFT_KEY);
		remGestureToUp = (ListPreference)findPreference(TO_UP_KEY);
		remGestureToDown = (ListPreference)findPreference(TO_DOWN);
	}
	class MyListPreferenceChangelistener implements OnPreferenceChangeListener {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
