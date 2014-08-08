package net.sharermax.mword;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AboutActivity extends Activity {

	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
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
//					Log.v("listview", "3");
					break;
				case 4:
//					Log.v("listview", "4");
					Intent intent4 = new Intent(Intent.ACTION_SENDTO);
					intent4.setData(Uri.parse("mailto:mdcw1103@gmail.com"));
					startActivity(Intent.createChooser(intent4, "请选择邮件类应用")); 
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
		return data;
	}
	
	
}
