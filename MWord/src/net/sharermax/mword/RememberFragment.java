package net.sharermax.mword;

import android.R.integer;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RememberFragment extends Fragment {

	private final static String REM_FONT_SIZE_KEY = "rem_font_size_key";
	private final static String REM_FONT_COLOR_KEY = "rem_font_color_key";
	private TextView rem_word_show;
	private TextView rew_des_show;
	private ImageView rem_query_image;
	private GestureDetector gestureDetector;
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
		String remFontColorString = sharedPreferences.getString(REM_FONT_COLOR_KEY, "000000");
//		int remFontColor = sharedPreferences.getInt(REM_FONT_COLOR_KEY, Integer.parseInt("0xff000000", 16));
		int remFontColor = Integer.parseInt(remFontColorString, 16) | 0xff000000;
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
		rem_query_image = (ImageView)(getView().findViewById(R.id.rem_query_image));
		rew_des_show.setLongClickable(true);
		
		MyGestureDetector myGestureDetector = new MyGestureDetector();
		gestureDetector = new GestureDetector(getActivity(), myGestureDetector);
		
		rew_des_show.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});
		
		rem_query_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("imageview", "OK");
				rem_query_image.setEnabled(false);
				rem_query_image.setVisibility(View.GONE);
			}
		});
		
	}
	public class MyGestureDetector  extends SimpleOnGestureListener{

		@Override
		public boolean onDown(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX,
				float velocityY) {
			// TODO Auto-generated method stub
			if ((event1.getX() - event2.getX() > 100) && (velocityX > 100)) {
				Log.v("touchevent", "to left");
				rem_query_image.setEnabled(true);
				rem_query_image.setVisibility(View.VISIBLE);
			} else if ((event2.getX() - event1.getX() > 100) && (velocityX > 100)){
				Log.v("touchevent", "to right");
			} else if ((event1.getY() - event2.getY() > 100) && (velocityY > 100)) {
				Log.v("touchevent", "to up");
			} else if((event2.getY() - event1.getY() > 100) && (velocityY > 100)) {
				Log.v("touchevent", "to down");
			}
			return true;
		}

		@Override
		public void onLongPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
				float arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onShowPress(MotionEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean onSingleTapUp(MotionEvent arg0) {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
	
}
