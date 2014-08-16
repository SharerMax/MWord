package net.sharermax.mword;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.readystatesoftware.systembartint.SystemBarTintManager.SystemBarConfig;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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
		
		if (android.os.Build.VERSION.SDK_INT > 18) {
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setTintColor(Color.parseColor("#ff009688"));
			
			SystemBarConfig systemBarConfig = tintManager.getConfig();
			findViewById(R.id.basicsetting).setPadding(0, systemBarConfig.getPixelInsetTop(getActionBar().isShowing()), 0, systemBarConfig.getPixelInsetBottom());
		}
		
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
