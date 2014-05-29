package net.sharermax.mword;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	private RememberFragment rememberFragment;
	private TranslateFragment searchFragment;
	private FragmentManager fragmentManager;
	private boolean currentFragment;
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
				fragmentManager.beginTransaction().replace(R.id.content, searchFragment).commit();
				
			}else {
				fragmentManager.beginTransaction().replace(R.id.content, rememberFragment).commit();
			}
			currentFragment = !currentFragment;
			return true;
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(this, SettingActivity.class);
			startActivity(intent);
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
