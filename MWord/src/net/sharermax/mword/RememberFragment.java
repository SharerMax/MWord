package net.sharermax.mword;


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
import android.app.ProgressDialog;
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
	private TextView rem_des_show_des_show;
	private ImageView rem_query_image;
	private GestureDetector gestureDetector;
	private int toRightAction;
	private int toLeftAction;
	private int toUpAction;
	private int toDownAction;
	private DBAdapter dbAdapter = null;
	private Word mWords[];
	private int mWordCount = 0;
	private int mRemWordFontSize;
	private int mRemWordFontColor;
	private int mRemDesFontSize;
	private int mRemDesFontColor;
	private static MainActivity activity;
	private ProgressDialog mImportProgressDialog;
	private boolean mImportOver = true;
	
	public RememberFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		if (activity instanceof MainActivity) {
			this.activity = (MainActivity)activity;
		}
	}

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

		readConfigFromPreference();
		applyConfigFromPreference();
		
		if (dbAdapter == null || dbAdapter.isClose()) {
			dbAdapter = new DBAdapter(activity);
			dbAdapter.open();
		}
		
		mWords = dbAdapter.queryAllData();
		// # bug : mWords is NULL
		// fixed
		
		//无单词提示
		if (mWords == null ) {
			if (mImportOver) {
				new NoWordDialogFragment().show(getFragmentManager(), null);
			}
			
		} else {
			rem_word_show.setText(mWords[0].spelling);
		}
	}

	public void applyConfigFromPreference() {
		rem_word_show.setTextColor(mRemWordFontColor);
		rem_word_show.setTextSize(TypedValue.COMPLEX_UNIT_SP, (mRemWordFontSize +  1) * 10);
		rem_des_show_des_show.setTextColor(mRemDesFontColor);
		rem_des_show_des_show.setTextSize(TypedValue.COMPLEX_UNIT_SP, (mRemDesFontSize +  1) * 10);
	}

	public void readConfigFromPreference() {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
		
		String toRightActionString = sharedPreferences.getString(PreferenceKey.REM_GESTURE_TORIGHT_KEY, "1");
		String toLeftActionString = sharedPreferences.getString(PreferenceKey.REM_GESTURE_TOLEFT_KEY, "2");
		String toUpActionSting = sharedPreferences.getString(PreferenceKey.REM_GESTURE_TOUP_KEY, "0");
		String toDownActionString = sharedPreferences.getString(PreferenceKey.REM_GESTURE_TODOWN_KEY, "0");
		mRemWordFontSize = sharedPreferences.getInt(PreferenceKey.REM_WORD_FONT_SIZE_KEY, 2);
		mRemDesFontSize = sharedPreferences.getInt(PreferenceKey.REM_DES_FONT_SIZE_KEY, 1);
		mRemWordFontColor = sharedPreferences.getInt(PreferenceKey.REM_WORD_FONT_COLOR_KEY, 0xff000000);
		mRemDesFontColor = sharedPreferences.getInt(PreferenceKey.REM_DES_FONT_COLOR_KEY, 0xff000000);
		toRightAction = Integer.parseInt(toRightActionString);
		toLeftAction = Integer.parseInt(toLeftActionString);
		toUpAction = Integer.parseInt(toUpActionSting);
		toDownAction = Integer.parseInt(toDownActionString);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		rem_word_show = (TextView)(getView().findViewById(R.id.rem_word_show));
		rem_des_show_des_show = (TextView)(getView().findViewById(R.id.rem_des_show));
		rem_query_image = (ImageView)(getView().findViewById(R.id.rem_query_image));
		rem_des_show_des_show.setLongClickable(true);
		
		MyGestureDetector myGestureDetector = new MyGestureDetector();
		gestureDetector = new GestureDetector(getActivity(), myGestureDetector);
		
		//解释区触摸事件监听
		rem_des_show_des_show.setOnTouchListener(new OnTouchListener() {
			
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
				rem_query_image.setEnabled(false);
				rem_query_image.setVisibility(View.GONE);
				rem_des_show_des_show.setText(mWords[mWordCount].explanation);
			}
		});
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		mWordCount = 0;
		super.onStop();
		dbAdapter.close();
		dbAdapter = null;
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
				gestureAction(toLeftAction);
			} else if ((event2.getX() - event1.getX() > 100) && (Math.abs(velocityX) > 150)){
				gestureAction(toRightAction);
			} else if ((event1.getY() - event2.getY() > 100) && (Math.abs(velocityY) > 150)) {
				gestureAction(toUpAction);
			} else if((event2.getY() - event1.getY() > 100) && (Math.abs(velocityY) > 150)) {
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
			break;
		case 1:
			if (mWordCount == 0) {
				Toast.makeText(getActivity(), getString(R.string.first_word_info), Toast.LENGTH_SHORT).show();
			} else {
				mWordCount--;
				rem_query_image.setEnabled(true);
				rem_query_image.setVisibility(View.VISIBLE);
				rem_word_show.setText(mWords[mWordCount].spelling);
				rem_des_show_des_show.setText("");
			}
			break;
		case 2:
			Log.v(TAG, "Next "+ (mWords.length - 1));
			if (mWordCount == (mWords.length - 1)) {
				Toast.makeText(getActivity(), getString(R.string.final_word_info), Toast.LENGTH_SHORT).show();
			} else {
				mWordCount++;
				rem_query_image.setEnabled(true);
				rem_query_image.setVisibility(View.VISIBLE);
				rem_word_show.setText(mWords[mWordCount].spelling);
				rem_des_show_des_show.setText("");
			}
			break;
		case 3:
			break;
		case 4:
			AlertDialog.Builder builder = new Builder(getActivity());
			builder.setTitle("删除？");
			builder.setMessage("你确定要删除这个单词？");
			builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					dbAdapter.deleteOneData(mWords[mWordCount]._id);
					Toast.makeText(getActivity(), "删除成功 :) ", Toast.LENGTH_SHORT).show();
					rem_query_image.setEnabled(true);
					rem_query_image.setVisibility(View.VISIBLE);
					mWords = dbAdapter.queryAllData();
					if ((mWordCount != 0) && (mWordCount >= mWords.length)) {
						mWordCount = mWords.length-1;
						
						rem_word_show.setText(mWords[mWordCount].spelling);
						rem_des_show_des_show.setText("");
					}
					if (mWords == null) {
						rem_word_show.setText("");
						rem_des_show_des_show.setText("");
						new NoWordDialogFragment().show(getFragmentManager(), null);
					}
				}
			});
			builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
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
					getActivity().finish();
				}
			});
			//Negative 按钮的设置 left switch to translate
			builder.setNegativeButton(getString(R.string.switching_translate), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					getFragmentManager().beginTransaction().replace(R.id.content, activity.getTranslateFragment()).commit();
					activity.setCurrentFragment(false);
				}
			});
			//NeutralButton 按钮设置 middle
			builder.setNeutralButton(getString(R.string.action_import), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("*/*");
					intent.addCategory(Intent.CATEGORY_OPENABLE);
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
		
//		Log.v("result", ""+requestCode);
		if (requestCode == 1) {	
			//click cancel of chooser
			if (data == null) {
				Toast.makeText(getActivity(), "无单词导入", Toast.LENGTH_SHORT).show();
				if (mWords == null) {
					new NoWordDialogFragment().show(getFragmentManager(), null);
				}	
			} else {
				Uri uri = data.getData();
				final String filename = uri.getPath();
				if (dbAdapter == null || dbAdapter.isClose()) {
					dbAdapter = new DBAdapter(activity);
					dbAdapter.open();
				}
				
				if (mImportProgressDialog == null) {
					mImportProgressDialog = ProgressDialog.show(activity, "导入", null);
				}
				mImportProgressDialog.setCanceledOnTouchOutside(false);
				mImportProgressDialog.setCancelable(false);
				mImportOver = false;
				final XmlAdapter xmlAdapter = new XmlAdapter();
				
				final Handler handler = new Handler() {

					@Override
					public void handleMessage(Message msg) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "导入单词数:" + msg.what, Toast.LENGTH_LONG).show();
						mImportProgressDialog.dismiss();
						mImportOver = true;
						mWords = dbAdapter.queryAllData();
						Log.v("REM_IMPORT", "" + mWords.length);
						if (mWords == null) {
							new NoWordDialogFragment().show(getFragmentManager(), null);
						} else {
							rem_word_show.setText(mWords[mWordCount].spelling);
							rem_des_show_des_show.setText("");
						}
						dbAdapter.close();
						
					}
					
				};
				new Thread() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Message msg = handler.obtainMessage();
						msg.what = xmlAdapter.xmlImport(dbAdapter, filename);
						handler.sendMessage(msg);
					}
					
				}.start();
				
			}
			
		}
	}
		
}
