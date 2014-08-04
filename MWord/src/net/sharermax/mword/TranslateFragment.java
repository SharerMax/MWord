package net.sharermax.mword;
/********************
 * 在线翻译主界面 TranslateFragment
 * author: SharerMax
 * create: 2014.05.29
 * modify: 2014.06.22
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import net.sharermax.mword.database.DBAdapter;
import net.sharermax.mword.database.Word;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class TranslateFragment extends Fragment {

	private Button translateButton;
	private TextView translate_des;
	private Handler handler;
	private NetWorkThread myThread;
	private RadioGroup translateGroup;
	private EditText translatInput;
	private Button addtoremButton;
	private String translate_word;
	private DBAdapter dbAdapter = null;
	private String translate_dst;
	private String translate_src;
	private static MainActivity activity;
	
	private final static String HOST_URL = "http://openapi.baidu.com/public/2.0/bmt/translate";
	private final static String CLIENT_ID = "w9fgEYaMcEl5MQ47OYK97RVM";
	private String from;
	private String to;
	
	
	
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
		return inflater.inflate(R.layout.translate_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		translateButton = (Button)(getView().findViewById(R.id.translate_button));
		translate_des = (TextView)(getView().findViewById(R.id.translate_des_show));
		translateGroup = (RadioGroup)(getView().findViewById(R.id.translate_radiogroup));
		translatInput = (EditText)(getView().findViewById(R.id.translate_input));
		addtoremButton = (Button)(getView().findViewById(R.id.addto_rem_button));
		from = "en";
		to = "zh";
		translate_src = "";
		translate_dst = "";
		//选择翻译模式
		translateGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				translate_des.setText("");
				translatInput.setText("");
				translate_src = "";
				translate_dst = "";
				switch (arg1) {
				case R.id.zhtoen_radiobutton:
					from = "zh";
					to = "en";
					break;
				case R.id.entozh_radiobutton:
					from = "en";
					to = "zh";
				default:
					break;
				}
			}
		});
		
		
		//点击开始查询
		translateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Log.v("Translate", "TTTTT");
				translate_word = translatInput.getText().toString();
				
				
				if (translate_word.equals("")) {
					Toast.makeText(getActivity(), "请输入准备查询的词", Toast.LENGTH_LONG).show();
				} else {
					setEditTextLostFocus();
					hideKeyboard();
					handler = new MyHandler();
					myThread = new NetWorkThread();
					myThread.start();
				}
			}
		});
		//添加查询的词到词库
		addtoremButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (translate_src.equals("") || translate_dst.equals("")) {
					Toast.makeText(getActivity(), "添加的单词为空", Toast.LENGTH_SHORT).show();
					return;
				} else {
					
				}
				Word word = new Word();
				if (from.equals("en")) {
					word.spelling = translate_src;
					word.explanation = translate_dst;
				} else {
					word.spelling = translate_dst;
					word.explanation = translate_src;
				}
				
				if (dbAdapter.insert(word) == -1) {
					Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (dbAdapter == null) {
			dbAdapter = new DBAdapter(getActivity());
			dbAdapter.open();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		Log.v("translate", "stop");
		dbAdapter.close();
		dbAdapter = null;
	}

	//请求数据，并对Json数据进行解析
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
//			Log.v("json", (String)msg.obj);
			try {
				JSONObject jsonObject = new JSONObject((String)msg.obj);
				if (jsonObject.optBoolean("error_code")) {
					switch (Integer.parseInt(jsonObject.getString("error_code"))) {
					case 52001:
						Toast.makeText(getActivity(), "TIMEOUT", Toast.LENGTH_LONG).show();
						break;
					case 52002:
						Toast.makeText(getActivity(), "SYSTEM ERROR", Toast.LENGTH_LONG).show();
					case 52003:
						Toast.makeText(getActivity(), "UNAUTHORIZED USER", Toast.LENGTH_LONG).show();
					default:
						break;
					}
				} else {
					JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
					JSONObject resultJsonObject = (JSONObject)jsonArray.opt(0);
					translate_dst = resultJsonObject.getString("dst");
					translate_src = resultJsonObject.getString("src");
					translate_des.setText(translate_dst);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	class NetWorkThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
				String httpUrl = HOST_URL + "?" +
						"client_id=" + CLIENT_ID +
						"&q=" + URLEncoder.encode(translate_word, "UTF-8").toString() +
						"&from=" + from +
						"&to=" + to;
//				Log.v("Thread", httpUrl);
//				HttpGet httpGet = new HttpGet(httpUrl);
//				BasicHttpParams params = new BasicHttpParams();
//				//连接超时2s
//				HttpConnectionParams.setConnectionTimeout(params, 2000);
//				//请求超时2s
//				HttpConnectionParams.setSoTimeout(params, 2000);
//				HttpClient httpClient = new DefaultHttpClient(params);
//				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpClient httpClient = new DefaultHttpClient();
				
				//连接超时 3s
				HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 3000);
				//读取超时 3s
				HttpConnectionParams.setSoTimeout(httpClient.getParams(), 3000);
				HttpGet httpGet = new HttpGet(httpUrl);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					String strResult = EntityUtils.toString(httpResponse.getEntity());
					Message msg = handler.obtainMessage();
					msg.obj = strResult;
					handler.sendMessage(msg);
					Log.v("network", "ok");
				} else {
					Log.v("network", "oh ,no");
				}
			} catch (java.net.ConnectException e) {
				// TODO: handle exception
				Toast.makeText(getActivity(), "连接超时", Toast.LENGTH_SHORT).show();
				Log.v("network", "connectException");
			} catch (java.net.SocketTimeoutException e) {
				// TODO: handle exception
				Toast.makeText(getActivity(), "请求超时", Toast.LENGTH_SHORT).show();
				Log.v("network", "connectException");
			}catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			Log.v("WORKTHREAD", Thread.currentThread().getName());
		}
		
	}
	
	public void setEditTextLostFocus() {
		translate_des.setFocusable(true);
		translate_des.setFocusableInTouchMode(true);
		translate_des.requestFocus();
		
	}
	
	public void hideKeyboard() {
		InputMethodManager inputMethodManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(translatInput.getWindowToken(), inputMethodManager.HIDE_NOT_ALWAYS);
	}
}
