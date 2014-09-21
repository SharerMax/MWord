package net.sharermax.mword;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sharermax.mword.network.UpdateApp;
import net.sharermax.mword.network.UpdateApp.TaskOverListener;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.readystatesoftware.systembartint.SystemBarTintManager.SystemBarConfig;

public class AboutActivity extends Activity {

	private ListView listView;
	private boolean mImmersionEnable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
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
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setTintColor(Color.parseColor("#ff009688"));
			SystemBarConfig systemBarConfig = tintManager.getConfig();
			findViewById(R.id.about_listview).setPadding(
					0, systemBarConfig.getPixelInsetTop(getActionBar().isShowing()), 
					0, systemBarConfig.getPixelInsetBottom());
		} else {
			mImmersionEnable = false;
		}
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		listView = (ListView)findViewById(R.id.about_listview);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData());
//		listView.setAdapter(adapter);
//		listView.setText
		SimpleAdapter adapter = new SimpleAdapter(this, getData(), R.layout.about_item, 
				new String[]{"text1", "text2"}, 
				new int[]{R.id.text1, R.id.text2});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				
				switch (arg2) {
				case 0:
//					Log.v("listview", "0");
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("http://sharermax.net/%E5%85%B3%E4%BA%8E%E6%88%91/"));
					startActivity(intent);
					break;
				case 1:
//					Log.v("listview", "1");
					Intent intent1 = new Intent(Intent.ACTION_VIEW);
					intent1.setData(Uri.parse("http://git.oschina.net/watermelon/MWord"));
					startActivity(intent1);
					break;
				case 2:
//					Log.v("listview", "2");
					Intent intent2 = new Intent(Intent.ACTION_VIEW);
					intent2.setData(Uri.parse("http://git.oschina.net/watermelon/MWord/raw/master/LICENSE"));
					startActivity(intent2);
					break;
				case 3:
					break;
				case 4:
//					Log.v("listview", "4");
					Intent intent4 = new Intent(Intent.ACTION_SENDTO);
					intent4.setData(Uri.parse("mailto:mdcw1103@gmail.com"));
					startActivity(Intent.createChooser(intent4, "请选择邮件类应用")); 
					break;
				case 5:
//					Log.v("listview", "5");
					UpdateApp updateApp = new UpdateApp(AboutActivity.this, UpdateApp.VERSION_TYPE_BETA);
					updateApp.setTaskOverListener(new TaskOverListener() {
						
						@Override
						public void taskOver(int versionCode) {
							// TODO Auto-generated method stub
							PackageManager packageManager = AboutActivity.this.getPackageManager();
							try {
								PackageInfo packageInfo = packageManager.getPackageInfo(AboutActivity.this.getPackageName(), 0);
//								Log.v("UPDATE", "" + versionCode);
								if (versionCode > packageInfo.versionCode) {
									
									Toast.makeText(getApplicationContext(), "有更新", Toast.LENGTH_LONG).show();
									updateNotification();
								} else {
									Toast.makeText(getApplicationContext(), "已是最新版", Toast.LENGTH_LONG).show();
								}
							} catch (NameNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
					updateApp.startTask();
					break;
				default:
					break;
				}
				
			}
		});
	}
	
	private List<HashMap<String, String>> getData() {
		List<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
//		data.add("开发者");
//		data.add("版本");
//		data.add("项目地址");
//		data.add("开源协议");
		
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("text1", "开发者");
		map.put("text2", "SharerMax");
		data.add(map);
		
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put("text1", "项目地址");
		map1.put("text2", "http://git.oschina.net/watermelon/MWord");
		data.add(map1);
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put("text1", "开源协议");
		map2.put("text2", "GPL V2");
		data.add(map2);
		
		PackageManager pManager = this.getPackageManager();
		try {
			PackageInfo info = pManager.getPackageInfo(this.getPackageName(), 0);
			String appversionname = info.versionName;
			HashMap<String, String> map3 = new HashMap<String, String>();
			map3.put("text1", "版本");
			map3.put("text2", appversionname);
			data.add(map3);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			HashMap<String, String> map3 = new HashMap<String, String>();
			map3.put("text1", "版本");
			map3.put("text2", "未知");
			data.add(map3);
		}

		HashMap<String, String> map4 = new HashMap<String, String>();
		map4.put("text1", "Bug反馈");
		map4.put("text2", "E-mail:mdcw1103@gmail.com");
		data.add(map4);
		
		HashMap<String, String> map5 = new HashMap<String, String>();
		map5.put("text1", "检查更新");
		map5.put("text2", "点击检查更新");
		data.add(map5);
		return data;
	}
	
	private void updateNotification() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		builder.setLargeIcon(bitmap);
		builder.setContentText("MWord有更新~~");
		builder.setContentTitle("MWord");
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("MWord有更新~~");
		builder.setAutoCancel(true);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(UpdateApp.APP_BETA_DOWNLOAD));
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//		Notification notification = builder.build();
//		notification.
		nManager.notify(0, builder.build());
		
	}
}
