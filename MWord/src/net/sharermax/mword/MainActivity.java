package net.sharermax.mword;
/********************
 * 主Activity 存在两个Fragment 包括RememberFragment、TranslateFragment 默认显示RememberFragment
 * author: SharerMax
 * create: 2014.05.27
 * modify: 2014.06.11
 */
import java.security.PublicKey;

import net.sharermax.mword.database.DBAdapter;
import net.sharermax.mword.xmlparse.XmlAdapter;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends Activity {

	private RememberFragment rememberFragment;
	private TranslateFragment translateFragment;
	private FragmentManager fragmentManager;
	private boolean currentFragment;
	private long exitTime;
	private ProgressDialog progressDialog;
	private DBAdapter dbAdapter;
	private Handler handler;
	private XmlAdapter xmlAdapter;
	private String expotrpath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rememberFragment = new RememberFragment(this);
		translateFragment = new TranslateFragment();
		
		fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content, rememberFragment).commit();
		currentFragment = true;
		
	}
	
	public TranslateFragment getFragment() {
		return translateFragment;
	}
	public void setCurrentFragment(boolean flag) {
		this.currentFragment = flag;
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
 				fragmentManager.beginTransaction().replace(R.id.content, translateFragment).commit();
				
			}else {
				//switch to Remember Fragment
				fragmentManager.beginTransaction().replace(R.id.content, rememberFragment).commit();
			}
			currentFragment = !currentFragment;
			return true;
		case R.id.action_settings:
			Intent intent = new Intent();
			intent.setClass(this, SettingPreferenceActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_import:
			Log.v("oooooppppptttt", "importaction");
			AlertDialog.Builder builder = new Builder(this);
		  	builder.setMessage("确认退出吗？");
		  	builder.setTitle("提示");
		  	builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
		  	});
		  	builder.create().show();
			return true;
		case R.id.action_export:
			Log.v("oooooppppptttt", "exportaction");
			CreateExportProgressDialog();
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
				Toast.makeText(MainActivity.this, "2秒内再次点击返回退出应用", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis(); 
			} else { 
				finish(); 
				System.exit(0); 
			} 
				return true; 
			} 
			return super.onKeyDown(keyCode, event); 
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
//		Log.v("result", ""+requestCode);
//		if (requestCode == 1) {
//			Uri uri = data.getData();
//			String filename = uri.toString().substring(uri.toString().lastIndexOf("/")+1);
//			Log.v("FileSelect", filename);
//		}
		//send data to fragment
		Log.v("result", ""+requestCode);
		if (requestCode == 1) {
			rememberFragment.onActivityResult(requestCode, resultCode, data);
		}
		
	}
	private void CreateExportProgressDialog() {
		progressDialog = ProgressDialog.show(this, "Exportting", null);
		progressDialog.setCanceledOnTouchOutside(false);
		dbAdapter = new DBAdapter(MainActivity.this);
		dbAdapter.open();
		Log.v("createexport", Thread.currentThread().getName());
		expotrpath = Environment.getExternalStorageDirectory().getPath() + "/MWord/Backup";
		xmlAdapter = new XmlAdapter();
		handler = new MyHandler();
		Thread thread = new ExpotrThread();
		thread.start();
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
			Log.v("handle", "handlemessage " + Thread.currentThread().getName());
			progressDialog.dismiss();
			Toast.makeText(MainActivity.this, "Export Number:" + msg.what, Toast.LENGTH_LONG).show();
			
		}
		
	}
}
