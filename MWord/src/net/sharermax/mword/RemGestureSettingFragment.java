package net.sharermax.mword;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;

public class RemGestureSettingFragment extends PreferenceFragment {

	private ListPreference remGestureToRight;
	private ListPreference remGestureToLeft;
	private ListPreference remGestureToUp;
	private ListPreference remGestureToDown;
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.remembergesture);
		remGestureToRight = (ListPreference)findPreference(PreferenceKey.REM_GESTURE_TORIGHT_KEY);
		remGestureToLeft = (ListPreference)findPreference(PreferenceKey.REM_GESTURE_TOLEFT_KEY);
		remGestureToUp = (ListPreference)findPreference(PreferenceKey.REM_GESTURE_TOUP_KEY);
		remGestureToDown = (ListPreference)findPreference(PreferenceKey.REM_GESTURE_TODOWN_KEY);
//		remFontColor = (ListPreference)findPreference(PreferenceKey.REM_FONT_COLOR_KEY);
		
//		translateApi = (ListPreference)findPreference(PreferenceKey.TRANSLATE_API_KEY);
		
		remGestureToRight.setSummary(remGestureToRight.getEntry());
		remGestureToLeft.setSummary(remGestureToLeft.getEntry());
		remGestureToUp.setSummary(remGestureToUp.getEntry());
		remGestureToDown.setSummary(remGestureToDown.getEntry());
//		
//		translateApi.setSummary(translateApi.getEntry());
//		
		GesturePreferenceChangelistener myGesturePreferenceChangelistener = new GesturePreferenceChangelistener();
		remGestureToRight.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		remGestureToLeft.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		remGestureToUp.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		remGestureToDown.setOnPreferenceChangeListener(myGesturePreferenceChangelistener);
		
//		remFontColor.setOnPreferenceChangeListener(new FontColorPreferenceChangeListener());
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
}
