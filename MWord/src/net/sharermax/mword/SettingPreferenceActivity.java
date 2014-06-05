package net.sharermax.mword;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
		//notes first argument is android.R.id.content ,is not R.id.content!!!
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
