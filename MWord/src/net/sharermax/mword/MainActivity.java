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

	private RememberFragment mRememberFragment;
	private TranslateFragment mTranslateFragment;
	private FragmentManager mFragmentManager;
	private boolean mIsRememberFragment; //true : RememberFragment false:TranslateFragment
	private long mExitTime;
	private ProgressDialog mProgressDialog;
	private DBAdapter mDBAdapter;
	private Handler mHandler;
	private XmlAdapter mXmlAdapter;
	private String mExpotrpath;
	private SlidingMenu mSlidingMenu;
	private boolean mImmersionEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mRememberFragment = new RememberFragment();
		mTranslateFragment = new TranslateFragment();
		
		mFragmentManager = getFragmentManager();
		
		mFragmentManager.beginTransaction().replace(R.id.content, mRememberFragment).commit();
		mIsRememberFragment = true;

		mSlidingMenu = new SlidingMenu(this);
		mSlidingMenu.setMode(SlidingMenu.LEFT);
		mSlidingMenu.setFadeDegree(0.35f);
		mSlidingMenu.setShadowDrawable(R.drawable.shadow);
		mSlidingMenu.setShadowWidth(15);
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		mSlidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		mSlidingMenu.setMenu(R.layout.menu);
		mSlidingMenu.setOnClosedListener(new MenuClosedListener());
		mSlidingMenu.setOnOpenListener(new MenuOpenListener());
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
		return mTranslateFragment;
	}
	public RememberFragment getRememberFragment() {
		return mRememberFragment;
	}
	public void setCurrentFragment(boolean flag) {
		this.mIsRememberFragment = flag;
	}
	public SlidingMenu getSlidingMenu() {
		return mSlidingMenu;
	}
	
	private boolean hideSlidingMenu() {
		// TODO Auto-generated method stub
		if (mSlidingMenu != null && mSlidingMenu.isMenuShowing()) {
			mSlidingMenu.toggle();
			return true;
		}
		return false;
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
		
		if (mSlidingMenu.isMenuShowing()) {
			mSlidingMenu.toggle();
		}

		switch (item.getItemId()) {
		case R.id.action_switching:
			if(mIsRememberFragment) {
				//switch to Translate Fragment
 				mFragmentManager.beginTransaction().replace(R.id.content, mTranslateFragment).commit();
 				item.setIcon(R.drawable.translate_indicator);
				
			}else {
				//switch to Remember Fragment
				mFragmentManager.beginTransaction().replace(R.id.content, mRememberFragment).commit();
				item.setIcon(R.drawable.remember_indicator);
			}
			mIsRememberFragment = !mIsRememberFragment;
			return true;
		case R.id.action_import:
			Intent fileselectintent = new Intent(Intent.ACTION_GET_CONTENT);
			fileselectintent.setType("*/*");
			fileselectintent.addCategory(Intent.CATEGORY_OPENABLE);
			//onActivityResult(...) method inside parent activity
			startActivityForResult(fileselectintent,2);
			return true;
		case R.id.action_export:
			CreateExportProgressDialog();
			return true;
		case android.R.id.home:
			mSlidingMenu.toggle();
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
			if((System.currentTimeMillis()-mExitTime) > 2000) { 
				//Toast.LENGTH_SHORT 2s
				//Toast.LENGTH_LONG 3.5s
				Toast.makeText(MainActivity.this, "再次点击即退出应用", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis(); 
			} else { 
				this.finish();
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
			mRememberFragment.onActivityResult(requestCode, resultCode, data);
			break;
		case 2:
			if (data != null) {
				Uri uri = data.getData();
				final String filename = uri.getPath();
				final XmlAdapter xmlAdapter = new XmlAdapter();
				final DBAdapter db = new DBAdapter(MainActivity.this);
				final Handler handler = new Handler() {
	
					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						Toast.makeText(MainActivity.this, "导入单词数:" + msg.arg1, Toast.LENGTH_LONG).show();
						db.close();
					}
					
				};
				new Thread() {
	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						db.open();
						Message msg = handler.obtainMessage();
						msg.arg1 = xmlAdapter.xmlImport(db, filename);
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
		mProgressDialog = ProgressDialog.show(this, "导出", null);
		mProgressDialog.setCanceledOnTouchOutside(false);
		if (mDBAdapter == null || mDBAdapter.isClose()) {
			mDBAdapter = new DBAdapter(MainActivity.this);
			mDBAdapter.open();
		}
		
		//检查SD卡是否存在，并生成导出路径
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			mExpotrpath = Environment.getExternalStorageDirectory().getPath() + "/MWord/Backup";
			mXmlAdapter = new XmlAdapter();
			mHandler = new MyHandler();
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
			Message msg = mHandler.obtainMessage();
			msg.what = mXmlAdapter.xmlExport(mDBAdapter, mExpotrpath);
			mHandler.sendMessage(msg);
		}
		
	}
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			mDBAdapter.close();
			mProgressDialog.dismiss();
			Toast.makeText(MainActivity.this, "导出单词数:" + msg.what, Toast.LENGTH_LONG).show();
			
		}
		
	}
	//when slidingMenu Closed 
	class MenuClosedListener implements OnClosedListener {
		@Override
		public void onClosed() {
			// TODO Auto-generated method stub
			if (mIsRememberFragment) {
				mRememberFragment.readConfigFromPreference();
				mRememberFragment.applyConfigFromPreference();
			}
		}
	}
	//when slidingMenu open
	class MenuOpenListener implements OnOpenListener {
		@Override
		public void onOpen() {
			// TODO Auto-generated method stub
			if (!mIsRememberFragment) {
				mTranslateFragment.setEditTextLostFocus();
				mTranslateFragment.hideKeyboard();
			}
		}
	}
}
