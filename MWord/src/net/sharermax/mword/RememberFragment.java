package net.sharermax.mword;
/********************
 * RememberFragment 记单词主界面
 * author: SharerMax
 * create: 2014.05.29
 * modify: 2014.08.04
 */

import net.sharermax.mword.database.DBAdapter;
import net.sharermax.mword.database.Word;
import net.sharermax.mword.xmlparse.XmlAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class RememberFragment extends Fragment {

	private TextView rem_word_show;
	private TextView rew_des_show;
	private ImageView rem_query_image;
	private GestureDetector gestureDetector;
	private int toRightAction;
	private int toLeftAction;
	private int toUpAction;
	private int toDownAction;
	private DBAdapter dbAdapter = null;
	private Word words[];
	private int wordcount = 0;
	private int remFontSize;
	private int remFontColor;
	private static MainActivity activity;
	
	public RememberFragment() {
		// TODO Auto-generated constructor stub
	}
	
//	public RememberFragment(MainActivity activity) {
//		// TODO Auto-generated constructor stub
//		this.activity = activity;
//	}
	//Activity 与 Fragment 建立联系时 call
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity)activity;
//			Log.v("Fragment", "MainActivity");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		dbAdapter = new DBAdapter(getActivity());
//		if (dbAdapter == null) {
//			dbAdapter = new DBAdapter(activity);
//			dbAdapter.open();
//		}
//		words = dbAdapter.queryAllData();
//		Log.v("Fragment", "oncreate");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		Log.v("Fragment", "oncreateview");
		
//		Log.v("wordsnumber", "" + wordcount);
		return inflater.inflate(R.layout.remember_layout, container, false);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		Log.v("Fragment", "onstart");

		readConfig();
		applyConfig();
		
		if (dbAdapter == null) {
			dbAdapter = new DBAdapter(activity);
			dbAdapter.open();
		}
		words = dbAdapter.queryAllData();
		
//		rem_word_show.setText(words[0].spelling);
		//无单词提示
		if (words == null) {
			new NoWordDialogFragment().show(getFragmentManager(), null);
		} else {
			rem_word_show.setText(words[0].spelling);
		}
	}

	public void applyConfig() {
		rem_word_show.setTextColor(remFontColor);
		rem_word_show.setTextSize(TypedValue.COMPLEX_UNIT_SP, (remFontSize +  1) * 10);
	}

	public void readConfig() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String remFontColorString = sharedPreferences.getString(PreferenceKey.REM_FONT_COLOR_KEY, "#000000").substring(1);
		String toRightActionString = sharedPreferences.getString(PreferenceKey.GESTURE_TORIGHT_KEY, "1");
		String toLeftActionString = sharedPreferences.getString(PreferenceKey.GESTURE_TOLEFT_KEY, "2");
		String toUpActionSting = sharedPreferences.getString(PreferenceKey.GESTURE_TOUP_KEY, "0");
		String toDownActionString = sharedPreferences.getString(PreferenceKey.GESTURE_TODOWN_KEY, "0");
		remFontSize = sharedPreferences.getInt(PreferenceKey.REM_FONT_SIZE_KEY, 2);
		remFontColor = Integer.parseInt(remFontColorString, 16) | 0xff000000;
		toRightAction = Integer.parseInt(toRightActionString);
		toLeftAction = Integer.parseInt(toLeftActionString);
		toUpAction = Integer.parseInt(toUpActionSting);
		toDownAction = Integer.parseInt(toDownActionString);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		Log.v("Fragment", "onactivitycreate");
		rem_word_show = (TextView)(getView().findViewById(R.id.rem_word_show));
		rew_des_show = (TextView)(getView().findViewById(R.id.rem_des_show));
		rem_query_image = (ImageView)(getView().findViewById(R.id.rem_query_image));
		rew_des_show.setLongClickable(true);
		
//		rem_word_show.setText(words[0].spelling);
		MyGestureDetector myGestureDetector = new MyGestureDetector();
		gestureDetector = new GestureDetector(getActivity(), myGestureDetector);
		
		//解释区触摸事件监听
		rew_des_show.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				return gestureDetector.onTouchEvent(event);
			}
		});
		//解释区等待显示提示图 单击监听
		rem_query_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Log.v("imageview", "OK");
				rem_query_image.setEnabled(false);
				rem_query_image.setVisibility(View.GONE);
				rew_des_show.setText(words[wordcount].explanation);
			}
		});
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
//		if (dbAdapter == null) {
//			Log.v("Fragment", "onpause null");
//		}
//		Log.v("Fragment", "onpause");
//		dbAdapter.close();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		Log.v("Fragment", "onresume1");
		
//		if (words == null) {
//			new NoWordDialogFragment().show(getFragmentManager(), null);
//			Log.v("Fragment", "null");
//		} else {
//			rem_word_show.setText(words[0].spelling);
//			Log.v("Fragment", "spelling" + words.length);
//		}
//		Log.v("Fragment", "onresume2");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		Log.v("Fragment", "onstop");
		dbAdapter.close();
		dbAdapter = null;
	}
	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
//		Log.v("fragment", "destory");
		super.onDestroy();
	}


	//Gesture 识别 
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
			if ((event1.getX() - event2.getX() > 100) && (Math.abs(velocityX) > 150)) {
//				Log.v("touchevent", "to left");
				
//				rem_query_image.setEnabled(true);
//				rem_query_image.setVisibility(View.VISIBLE);
				gestureAction(toLeftAction);
			} else if ((event2.getX() - event1.getX() > 100) && (Math.abs(velocityX) > 150)){
//				rem_query_image.setEnabled(true);
//				rem_query_image.setVisibility(View.VISIBLE);
//				Log.v("touchevent", "to right");
				gestureAction(toRightAction);
			} else if ((event1.getY() - event2.getY() > 100) && (Math.abs(velocityY) > 150)) {
//				Log.v("touchevent", "to up");
				gestureAction(toUpAction);
			} else if((event2.getY() - event1.getY() > 100) && (Math.abs(velocityY) > 150)) {
//				Log.v("touchevent", "to down");
				gestureAction(toDownAction);
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
	public void gestureAction(int action) {
		String TAG = "GestureAction";
		switch (action) {
		case 0:
//			Log.v(TAG, "NULL");
			break;
		case 1:
//			Log.v(TAG, "Previous");
			if (wordcount == 0) {
				Toast.makeText(getActivity(), getString(R.string.first_word_info), Toast.LENGTH_SHORT).show();
			} else {
				wordcount--;
				rem_query_image.setEnabled(true);
				rem_query_image.setVisibility(View.VISIBLE);
				rem_word_show.setText(words[wordcount].spelling);
				rew_des_show.setText("");
			}
			break;
		case 2:
			Log.v(TAG, "Next "+ (words.length - 1));
			if (wordcount == (words.length - 1)) {
				Toast.makeText(getActivity(), getString(R.string.final_word_info), Toast.LENGTH_SHORT).show();
			} else {
				wordcount++;
				rem_query_image.setEnabled(true);
				rem_query_image.setVisibility(View.VISIBLE);
				rem_word_show.setText(words[wordcount].spelling);
				rew_des_show.setText("");
			}
			break;
		case 3:
//			Log.v(TAG, "New");	
			break;
		case 4:
//			Log.v(TAG, "Delete");
			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setTitle("删除？");
			builder.setMessage("你确定要删除这个单词？");
			builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
//					Log.v("gestureAction", "delete_ok");
//					if (dbAdapter == null) {
//						Log.v("delete",""+ words[wordcount]._id);
//					}
//					Log.v("delete",""+ words[wordcount]._id);
					dbAdapter.deleteOneData(words[wordcount]._id);
					Toast.makeText(getActivity(), "删除成功 :) ", Toast.LENGTH_SHORT).show();
//					getFragmentManager().beginTransaction().replace(R.id.content, new RememberFragment(activity)).commit();
					rem_query_image.setEnabled(true);
					rem_query_image.setVisibility(View.VISIBLE);
					words = dbAdapter.queryAllData();
					if ((wordcount != 0) && (wordcount >= words.length)) {
						wordcount = words.length-1;
						
						rem_word_show.setText(words[wordcount].spelling);
						rew_des_show.setText("");
					}
					if (words == null) {
						rem_word_show.setText("");
						rew_des_show.setText("");
						new NoWordDialogFragment().show(getFragmentManager(), null);
					}
				}
			});
			builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
//					Log.v("gestureAction", "delete_cancel");
				}
			});
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			break;
		default:
			break;
		}
	}
	//无单词提示Dialog实现
	public static class NoWordDialogFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle(getString(R.string.note));
			builder.setMessage(getString(R.string.no_word_message));
			//positive 按钮的设置 right  exit
			builder.setPositiveButton(getString(R.string.app_exit), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
//					Log.v("Dialog", ""+arg1);
					getActivity().finish();
				}
			});
			//Negative 按钮的设置 left switch to translate
			builder.setNegativeButton(getString(R.string.switching_translate), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
//					Log.v("Dialog", ""+arg1);
					getFragmentManager().beginTransaction().replace(R.id.content, activity.getTranslateFragment()).commit();
					activity.setCurrentFragment(false);
				}
			});
			//NeutralButton 按钮设置 middle
			builder.setNeutralButton(getString(R.string.action_import), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
//					Log.v("Dialog", ""+arg1);
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("*/*");
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					//parent Activity start Activity，if I will get callback result need call
					//onActivityResult(...) method inside parent activity
					getActivity().startActivityForResult(intent,1);
				}
			});
			builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
				
				@Override
				public boolean onKey(DialogInterface arg0, int keycode, KeyEvent event) {
					// TODO Auto-generated method stub
					return activity.onKeyDown(keycode, event);
				}
			});
			Dialog dialog = builder.create();
			dialog.setCanceledOnTouchOutside(false);
			
			return dialog;
		}		
	}
	//receive result from activity
	public void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		// TODO Auto-generated method stub
		
		Log.v("result", ""+requestCode);
		if (requestCode == 1) {	
			//click cancel of chooser
			if (data == null) {
				Log.v("FileSelect", "null");
//				getFragmentManager().beginTransaction().replace(R.id.content, activity.getRememberFragment()).commit();
				if (words == null) {
					new NoWordDialogFragment().show(getFragmentManager(), null);
				}	
			} else {
				Uri uri = data.getData();
				final String filename = uri.getPath();
//				String filename = uri.toString().substring(uri.toString().lastIndexOf("/")+1);
				if (dbAdapter == null) {
					dbAdapter = new DBAdapter(activity);
					dbAdapter.open();
				}
				final XmlAdapter xmlAdapter = new XmlAdapter();
				
				final Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "导入单词数:" + msg.what, Toast.LENGTH_LONG).show();
						words = dbAdapter.queryAllData();
						if (words == null) {
							new NoWordDialogFragment().show(getFragmentManager(), null);
						} else {
							rem_word_show.setText(words[wordcount].spelling);
							rew_des_show.setText("");
						}
						
//						db.close();
//						getFragmentManager().beginTransaction().replace(R.id.content, activity.getRememberFragment()).commit();
						
					}
					
				};
//				Log.v("FileSelect", filename);
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
//						db.open();
						Message msg = handler.obtainMessage();
						msg.what = xmlAdapter.xmlImport(dbAdapter, filename);
//						Log.v("xmlimport", "TTT");
						handler.sendMessage(msg);
					}
					
				}.start();
				
			}
			
		}
	}
		
}
