package net.sharermax.mword;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class SettingFragment extends PreferenceFragment {

	private ListPreference remGestureToRight;
	private ListPreference remGestureToLeft;
	private ListPreference remGestureToUp;
	private ListPreference remGestureToDown;
	private ListPreference remFontColor;
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
		remFontColor = (ListPreference)findPreference("rem_font_color_key");
		
		remGestureToRight.setSummary(remGestureToRight.getEntry());
		remGestureToLeft.setSummary(remGestureToLeft.getEntry());
		remGestureToUp.setSummary(remGestureToUp.getEntry());
		remGestureToDown.setSummary(remGestureToDown.getEntry());
		
		GesturePreferenceChangelistener myGesturePreferenceChangelistener = new GesturePreferenceChangelistener();
		remGestureToRight.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		remGestureToLeft.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		remGestureToUp.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		remGestureToDown.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		
		remFontColor.setOnPreferenceChangeListener(new FontColorPreferenceChangeListener());
	}
	class GesturePreferenceChangelistener implements OnPreferenceChangeListener {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			// TODO Auto-generated method stub
			if (preference instanceof ListPreference) {
				switch (Integer.parseInt((String)newValue)) {
				case 0:
					preference.setSummary(getString(R.string.null_action));
					break;
				case 1:
					preference.setSummary(getString(R.string.previous_action));
					break;
				case 2:
					preference.setSummary(getString(R.string.next_action));
					break;
				case 3:
					preference.setSummary(getString(R.string.new_action));
					break;
				case 4:
					preference.setSummary(getString(R.string.delete_action));
					break;
				default:
					return false;
				}
			}
			return true;
		}
	}
	class FontColorPreferenceChangeListener implements OnPreferenceChangeListener {
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			// TODO Auto-generated method stub
			if (preference instanceof ListPreference) {
				System.out.println(Integer.parseInt((String)newValue, 16));
			}
			return true;
		}
	}
}
