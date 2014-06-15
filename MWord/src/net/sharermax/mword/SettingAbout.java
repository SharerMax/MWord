package net.sharermax.mword;
/********************
 * 在PreferenceActivity中添加关于一项 
 * author: SharerMax
 * create: 2014.06.15
 * modify: 2014.06.15
 */
import android.content.Context;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;

public class SettingAbout extends Preference{
	public SettingAbout(Context context, AttributeSet attr) {
		// TODO Auto-generated constructor stub
		super(context, attr);
	}

	@Override
	protected void onClick() {
		// TODO Auto-generated method stub
		super.onClick();
		Intent intent = new Intent(getContext(), AboutActivity.class);
		getContext().startActivity(intent);
		
	}
	
}
