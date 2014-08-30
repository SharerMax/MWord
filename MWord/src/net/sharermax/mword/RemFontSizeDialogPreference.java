package net.sharermax.mword;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class RemFontSizeDialogPreference extends DialogPreference {
	
	private SeekBar fontsizeSeekBar;
	private TextView previewView;
	private int rem_front_size;
	public RemFontSizeDialogPreference(Context context, AttributeSet attrs, int defStyle) {
		// TODO Auto-generated constructor stub
		super(context, attrs, defStyle);
	}
	
	public RemFontSizeDialogPreference(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
//		
		
	}

	@Override
	protected void onBindDialogView(View view) {
		// TODO Auto-generated method stub
		super.onBindDialogView(view);
		
		fontsizeSeekBar = (SeekBar)(view.findViewById(R.id.rem_frontsize_Bar));
		previewView = (TextView)(view.findViewById(R.id.rem_frontsize_view));		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		String title = (String) getTitle();
		if (title.equals(getContext().getString(R.string.word_font_size))) {
			rem_front_size = sharedPreferences.getInt(PreferenceKey.REM_WORD_FONT_SIZE_KEY, 2);
		} else if(title.equals(getContext().getString(R.string.des_font_size))) {
			rem_front_size = sharedPreferences.getInt(PreferenceKey.REM_DES_FONT_SIZE_KEY, 1);
		} else {
			//default value
			rem_front_size = 1;
		}
		
		previewView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (rem_front_size + 1) * 10);
		fontsizeSeekBar.setProgress(rem_front_size);
		
		fontsizeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				rem_front_size = progress;
				previewView.setTextSize(TypedValue.COMPLEX_UNIT_SP, (rem_front_size + 1) * 10 );
			}
		});
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// TODO Auto-generated method stub
		super.onDialogClosed(positiveResult);
		if(positiveResult) {
			persistInt(rem_front_size);
		}
	}
	
}
