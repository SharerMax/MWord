package net.sharermax.mword;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RememberFragment extends Fragment {

	private final static String REM_FONT_SIZE_KEY = "rem_font_size_key";
	private final static String REM_FONT_COLOR_KEY = "rem_font_color_key";
	private TextView rem_word_show;
	private TextView rew_des_show;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.remember_layout, container, false);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		int remFontColor = sharedPreferences.getInt(REM_FONT_COLOR_KEY, 0xff000000);
		int remFontSize = sharedPreferences.getInt(REM_FONT_SIZE_KEY, 2);
		rem_word_show.setTextColor(remFontColor);
		rem_word_show.setTextSize(TypedValue.COMPLEX_UNIT_SP, (remFontSize +  1) * 10);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		rem_word_show = (TextView)(getView().findViewById(R.id.rem_word_show));
		rew_des_show = (TextView)(getView().findViewById(R.id.rem_des_show));
	}
	
}
