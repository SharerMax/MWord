package net.sharermax.mword;

import me.imid.swipebacklayout.SwipeBackLayout;
import me.imid.swipebacklayout.app.SwipeBackActivity;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.readystatesoftware.systembartint.SystemBarTintManager.SystemBarConfig;


import android.R.bool;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class BasicSettingActivity extends SwipeBackActivity {
	private String title;
	private Fragment fragment;
	private SwipeBackLayout mSwipeBackLayout;
	private boolean mImmersionEnable;
	private boolean mSwipeBackEnable;
	public BasicSettingActivity() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.basicsetting);
		
		SharedPreferences sharedPreferences = 
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if (android.os.Build.VERSION.SDK_INT > 18 && 
				sharedPreferences.getBoolean(PreferenceKey.IMMERSION_KEY, true)) {
			mImmersionEnable = true;
			Window window = getWindow();
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, 
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, 
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			
		} else {
			mImmersionEnable = false;
		}
		
		//滑动返回
		mSwipeBackEnable = sharedPreferences.getBoolean(PreferenceKey.SWIPE_BACK_KEY, true);
		mSwipeBackLayout = getSwipeBackLayout();
		if (mSwipeBackEnable) {
//			Log.v("BASIC", "YYY");
			mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
		} else {
			setSwipeBackEnable(false);
		}
		
		Intent intent = getIntent();
		title = intent.getStringExtra("SlidingFragment");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(title);
//		Log.v("basicactivity", title);
		if (title.equals(getString(R.string.setting_font_remember))) {
			fragment = new RemFontSettingFragment();
		} else if (title.equals(getString(R.string.setting_gesture))) {
			fragment = new RemGestureSettingFragment();
		} else if (title.equals(getString(R.string.app_advance_setteing))) {
			fragment = new AdvanceSettingFragment();
		}

		getFragmentManager().beginTransaction().replace(R.id.basicsetting, fragment).commit();
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 18 && mImmersionEnable) {
        	SystemBarTintManager tintManager = new SystemBarTintManager(this);
    		tintManager.setNavigationBarTintEnabled(true);
    		tintManager.setStatusBarTintEnabled(true);
    		tintManager.setTintColor(Color.parseColor("#ff009688"));
    		SystemBarConfig systemBarConfig = tintManager.getConfig();
    		findViewById(R.id.basicsetting).setPadding(
    				0, systemBarConfig.getPixelInsetTop(getActionBar().isShowing()), 
    				0, systemBarConfig.getPixelInsetBottom());
		}
    }
	
}
