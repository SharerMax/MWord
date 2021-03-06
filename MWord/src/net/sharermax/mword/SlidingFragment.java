package net.sharermax.mword;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class SlidingFragment extends PreferenceFragment {

	
//	private ListPreference remFontColor;
	private Preference remFontPreference;
	private Preference remGesturePreference;
	private ListPreference translateApi;
	private Preference aboutPreference;
	private Preference advancePreference;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.slidingmenu);
		
		remFontPreference = (Preference)findPreference(PreferenceKey.REM_SETIING_FONT_KEY);
		remGesturePreference = (Preference)findPreference(PreferenceKey.REM_SETTING_GESTURE_KEY);
		translateApi = (ListPreference)findPreference(PreferenceKey.TRANSLATE_API_KEY);
		aboutPreference = (Preference)findPreference(PreferenceKey.ABOUT_KEY);
		advancePreference = (Preference)findPreference(PreferenceKey.ADVANCE_SETTING_KEY);
		
		
		translateApi.setSummary(translateApi.getEntry());
		
		SlidingOnPreferenceClickListener slidingOnPreferenceClickListener = new SlidingOnPreferenceClickListener();
		remFontPreference.setOnPreferenceClickListener(slidingOnPreferenceClickListener);
		remGesturePreference.setOnPreferenceClickListener(slidingOnPreferenceClickListener);
		aboutPreference.setOnPreferenceClickListener(slidingOnPreferenceClickListener);
		advancePreference.setOnPreferenceClickListener(slidingOnPreferenceClickListener);
	}
	
	class SlidingOnPreferenceClickListener implements OnPreferenceClickListener {
		@Override
		public boolean onPreferenceClick(Preference preference) {
			// TODO Auto-generated method stub
			String preferenceTitle = (String) preference.getTitle();
			if (preferenceTitle.equals(getString(R.string.setting_font_remember))) {
				Intent intent = new Intent();
				intent.putExtra("SlidingFragment", getString(R.string.setting_font_remember));
				intent.setClass(getActivity(), BasicSettingActivity.class);
				startActivity(intent);
				return true;
			} else if (preferenceTitle.equals(getString(R.string.setting_gesture))) {
				Intent intent = new Intent();
				intent.putExtra("SlidingFragment", getString(R.string.setting_gesture));
				intent.setClass(getActivity(), BasicSettingActivity.class);
				startActivity(intent);
				return true;
			} else if (preferenceTitle.equals(getString(R.string.app_about))){
				Intent intent = new Intent();
				intent.setClass(getActivity(), AboutActivity.class);
				startActivity(intent);
				return true;
			} else if (preferenceTitle.equals(getString(R.string.app_advance_setteing))) {
				Intent intent = new Intent();
				intent.putExtra("SlidingFragment", getString(R.string.app_advance_setteing));
				intent.setClass(getActivity(), BasicSettingActivity.class);
				startActivity(intent);
				return true;
			} else {
				return false;
			}
			
		}
	}
}
