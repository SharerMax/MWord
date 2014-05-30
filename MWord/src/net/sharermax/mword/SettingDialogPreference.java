package net.sharermax.mword;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class SettingDialogPreference extends DialogPreference {
	
	public SettingDialogPreference(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
	}
	
	public SettingDialogPreference(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
	}
	
}
