package net.sharermax.mword;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	private RememberFragment rememberFragment;
	private TranslateFragment searchFragment;
	private FragmentManager fragmentManager;
	private boolean currentFragment;
	private long exitTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rememberFragment = new RememberFragment();
		searchFragment = new TranslateFragment();
		
	
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, rememberFragment).commit();
		currentFragment = true;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_switching:
			if(currentFragment) {
				//switch to Translate Fragment
 				fragmentManager.beginTransaction().replace(R.id.content, searchFragment).commit();
 				item.setTitle(getString(R.string.switching_remember));
				
			}else {
				//switch to Remember Fragment
				fragmentManager.beginTransaction().replace(R.id.content, rememberFragment).commit();
				item.setTitle(getString(R.string.switching_translate));
			}
			currentFragment = !currentFragment;
			return true;
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(this, SettingPreferenceActivity.class);
			startActivity(intent);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) { 
			if((System.currentTimeMillis()-exitTime) > 2000) { 
				Toast.makeText(getApplicationContext(), "2秒内再次点击返回退出应用", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis(); 
			} else { 
				finish(); 
				System.exit(0); 
			} 
				return true; 
			} 
			return super.onKeyDown(keyCode, event); 
	}
	
}
