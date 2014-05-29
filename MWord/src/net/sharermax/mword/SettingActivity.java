package net.sharermax.mword;

import android.app.Activity;
import android.os.Bundle;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_layout);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
