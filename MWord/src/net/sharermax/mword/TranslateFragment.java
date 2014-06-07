package net.sharermax.mword;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TranslateFragment extends Fragment {

	private Button translateButton;
	private TextView translate_des;
	private Handler handler;
	NetWorkThread myThread;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.translate_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		translateButton = (Button)(getView().findViewById(R.id.translate_button));
		translate_des = (TextView)(getView().findViewById(R.id.translate_des_show));
		handler = new MyHandler();
		translateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Log.v("Translate", "TTTTT");
				myThread = new NetWorkThread();
				myThread.start();
			}
		});
	}
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Log.v("handler", Thread.currentThread().getName());
		}
		
	}
	class NetWorkThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
			Log.v("WorkThread", "WWWWWW");
		}
		
	}
}
