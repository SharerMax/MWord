package net.sharermax.mword;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

public class BasicSettingActivity extends Activity {
	private String title;
	private Fragment fragment;
	public BasicSettingActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.basicsetting);
		Intent intent = getIntent();
		title = intent.getStringExtra("SlidingFragment");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(title);
		if (title.equals(getString(R.string.setting_font_remember))) {
			fragment = new RemFontSettingFragment();
		} else if (title.equals(getString(R.string.setting_gesture))) {
			fragment = new RemGestureSettingFragment();
		} 

		getFragmentManager().beginTransaction().replace(R.id.basicsetting, fragment).commit();
	}
	
	
}
