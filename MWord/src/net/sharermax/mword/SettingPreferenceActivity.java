package net.sharermax.mword;
/********************
 * 设置界面 PreferenceActivity 调用SettingFragment 保存设置项
 * author: SharerMax
 * create: 2014.05.30
 * modify: 2014.06.15
 */
import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingPreferenceActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingFragment()).commit();
		
		//notes first argument is android.R.id.content ,is not R.id.content!!!
//		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
