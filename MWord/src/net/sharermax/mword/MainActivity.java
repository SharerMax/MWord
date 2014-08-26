package net.sharermax.mword;

import net.sharermax.mword.database.DBAdapter;
import net.sharermax.mword.xmlparse.XmlAdapter;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

public class MainActivity extends Activity {

	private RememberFragment rememberFragment;
	private TranslateFragment translateFragment;
	private FragmentManager fragmentManager;
	private boolean currentFragment; //true : RememberFragment false:TranslateFragment
	private long exitTime;
	private ProgressDialog progressDialog;
	private DBAdapter dbAdapter;
	private Handler handler;
	private XmlAdapter xmlAdapter;
	private String expotrpath;
	private SlidingMenu slidingMenu;
	private boolean mImmersionEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rememberFragment = new RememberFragment();
		translateFragment = new TranslateFragment();
		
		fragmentManager = getFragmentManager();
		
		fragmentManager.beginTransaction().replace(R.id.content, rememberFragment).commit();
		currentFragment = true;

		slidingMenu = new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setFadeDegree(0.35f);
		slidingMenu.setShadowDrawable(R.drawable.shadow);
		slidingMenu.setShadowWidth(15);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		slidingMenu.setMenu(R.layout.menu);
		slidingMenu.setOnClosedListener(new MenuClosedListener());
		slidingMenu.setOnOpenListener(new MenuOpenListener());
		getFragmentManager().beginTransaction().replace(
				R.id.slidingmenu, new SlidingFragment(), "SettingFragment").commit();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		SharedPreferences sharedPreferences = 
				PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		
		if (android.os.Build.VERSION.SDK_INT > 18 &&
				sharedPreferences.getBoolean(PreferenceKey.IMMERSION_KEY, true)) {
			mImmersionEnable = true;
			Window window = getWindow();
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setTintColor(Color.parseColor("#ff009688"));
		} else {
			mImmersionEnable = false;
		}
		
	}
	
	public TranslateFragment getTranslateFragment() {
		return translateFragment;
	}
	public RememberFragment getRememberFragment() {
		return rememberFragment;
	}
	public void setCurrentFragment(boolean flag) {
		this.currentFragment = flag;
	}
	public SlidingMenu getSlidingMenu() {
		return slidingMenu;
	}
	
	private boolean hideSlidingMenu() {
		// TODO Auto-generated method stub
		if (slidingMenu != null && slidingMenu.isMenuShowing()) {
			slidingMenu.toggle();
			return true;
		}
		return false;
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hideSlidingMenu();
	}

	

	//Option Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		if (slidingMenu.isMenuShowing()) {
			slidingMenu.toggle();
		}

		switch (item.getItemId()) {
		case R.id.action_switching:
			if(currentFragment) {
				//switch to Translate Fragment
 				fragmentManager.beginTransaction().replace(R.id.content, translateFragment).commit();
 				item.setIcon(R.drawable.translate_indicator);
				
			}else {
				//switch to Remember Fragment
				fragmentManager.beginTransaction().replace(R.id.content, rememberFragment).commit();
				item.setIcon(R.drawable.remember_indicator);
			}
			currentFragment = !currentFragment;
			return true;
		case R.id.action_import:
//			Log.v("oooooppppptttt", "importaction");
			Intent fileselectintent = new Intent(Intent.ACTION_GET_CONTENT);
			fileselectintent.setType("*/*");
			fileselectintent.addCategory(Intent.CATEGORY_OPENABLE);
			//parent Activity start Activity，if I will get callback result need call
			//onActivityResult(...) method inside parent activity
			startActivityForResult(fileselectintent,2);
			return true;
		case R.id.action_export:
//			Log.v("oooooppppptttt", "exportaction");
			CreateExportProgressDialog();
			return true;
		case android.R.id.home:
//			Log.v("MainActivity", "home");
			slidingMenu.toggle();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	//2s内两次点击返回键退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) { 
			if (hideSlidingMenu()) {
				return true;
			}
//			Log.v("MenuShowing", msg)
			if((System.currentTimeMillis()-exitTime) > 2000) { 
				//Toast.LENGTH_SHORT 2s
				//Toast.LENGTH_LONG 3.5s
				Toast.makeText(MainActivity.this, "再次点击即退出应用", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis(); 
			} else { 
				this.finish();
//				System.exit(0); 
			} 
				return true; 
			} 
			return super.onKeyDown(keyCode, event); 
	}
	
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		// requestcode = 1 来着RememberFragment的文件选择
		// requestcode = 2 来着ActionBar的文件选择
		switch (requestCode) {
		case 1:
			rememberFragment.onActivityResult(requestCode, resultCode, data);
			break;
		case 2:
			if (data == null) {
//				Log.v("FileSelect", "null");
			} else {
				Uri uri = data.getData();
				final String filename = uri.getPath();
				final XmlAdapter xmlAdapter = new XmlAdapter();
				final DBAdapter db = new DBAdapter(MainActivity.this);
				final Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "导入单词数:" + msg.what, Toast.LENGTH_LONG).show();
						db.close();			
					}
					
				};
				Log.v("FileSelect", filename);
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						db.open();
						Message msg = handler.obtainMessage();
						msg.what = xmlAdapter.xmlImport(db, filename);
						handler.sendMessage(msg);
					}
					
				}.start();
			}
			break;
		default:
			break;
		}
		
	}
	//导出进度Dialog
	private void CreateExportProgressDialog() {
		progressDialog = ProgressDialog.show(this, "导出", null);
		progressDialog.setCanceledOnTouchOutside(false);
		dbAdapter = new DBAdapter(MainActivity.this);
		dbAdapter.open();
//		Log.v("createexport", Thread.currentThread().getName());
		//检查SD卡是否存在，并生成导出路径
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			expotrpath = Environment.getExternalStorageDirectory().getPath() + "/MWord/Backup";
			xmlAdapter = new XmlAdapter();
			handler = new MyHandler();
			Thread thread = new ExpotrThread();
			thread.start();
		} else {
			Toast.makeText(MainActivity.this, "SD卡不存在 :( ", Toast.LENGTH_LONG).show();
		}
		
	}
	
	class ExpotrThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = handler.obtainMessage();
			msg.what = xmlAdapter.xmlExport(dbAdapter, expotrpath);
			handler.sendMessage(msg);
		}
		
	}
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
//			Log.v("handle", "handlemessage " + Thread.currentThread().getName());
			dbAdapter.close();
			progressDialog.dismiss();
			Toast.makeText(MainActivity.this, "导出单词数:" + msg.what, Toast.LENGTH_LONG).show();
			
		}
		
	}
	//when slidingMenu Closed 
	class MenuClosedListener implements OnClosedListener {
		@Override
		public void onClosed() {
			// TODO Auto-generated method stub
			if (currentFragment) {
				rememberFragment.readConfigFromPreference();
				rememberFragment.applyConfigFromPreference();
			}
		}
	}
	//when slidingMenu open
	class MenuOpenListener implements OnOpenListener {
		@Override
		public void onOpen() {
			// TODO Auto-generated method stub
			if (!currentFragment) {
				translateFragment.setEditTextLostFocus();
				translateFragment.hideKeyboard();
			}
		}
	}
}
