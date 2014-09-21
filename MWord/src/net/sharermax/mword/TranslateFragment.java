package net.sharermax.mword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import net.sharermax.mword.database.DBAdapter;
import net.sharermax.mword.database.Word;
import net.sharermax.mword.network.NetworkConnection;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
	private RadioGroup translateGroup;
	private EditText translatInput;
	private Button addtoremButton;
	private String translate_word;
	private DBAdapter dbAdapter = null;
	private String translate_dst;
	private String translate_src;
	private  MainActivity activity;
	
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
			public void onCheckedChanged(RadioGroup arg0, int selection) {
				// TODO Auto-generated method stub
				translate_des.setText("");
				translatInput.setText("");
				translate_src = "";
				translate_dst = "";
				switch (selection) {
				case R.id.zhtoen_radiobutton:
					from = "zh";
					to = "en";
					break;
				case R.id.entozh_radiobutton:
					from = "en";
					to = "zh";
					break;
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
				translate_des.setText("");
				if (! new NetworkConnection(activity.getApplicationContext()).isConnected()) {
					Toast.makeText(getActivity(), "网络不可用(─.─|||", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if (translate_word.equals("")) {
					Toast.makeText(getActivity(), "请输入准备查询的词", Toast.LENGTH_LONG).show();
				} else {
					setEditTextLostFocus();
					hideKeyboard();
					String httpUrl;
					try {
						httpUrl = HOST_URL + "?" +
								"client_id=" + CLIENT_ID +
								"&q=" + URLEncoder.encode(translate_word, "UTF-8").toString() +
								"&from=" + from +
								"&to=" + to;
						TranslaterTask myTask = new TranslaterTask();
						myTask.execute(httpUrl);
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
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
		
		if (! new NetworkConnection(activity.getApplicationContext()).isConnected()) {
			Toast.makeText(getActivity(), "网络不可用(─.─|||", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
//		Log.v("translate", "stop");
		setEditTextLostFocus();
		hideKeyboard();
		dbAdapter.close();
		dbAdapter = null;
	}
	
	private class TranslaterTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... httpUrl) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(httpUrl[0]);
				Log.v("aysnctask", httpUrl[0]);
				HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
				httpURLConnection.setConnectTimeout(3000);
				httpURLConnection.setDoInput(true);
				httpURLConnection.setRequestMethod("GET");
				
				if (httpURLConnection.getResponseCode()== HttpStatus.SC_OK) {
					InputStream resultInputStream = httpURLConnection.getInputStream();
					
					BufferedReader resultBufferedReader = new BufferedReader(
							new InputStreamReader(resultInputStream));
					String strResult = "";
					String lineString = null;
					while ((lineString = resultBufferedReader.readLine()) != null) {
						strResult += lineString;
						Log.v("bufferredreader", strResult);
					}
					resultInputStream.close();
					resultBufferedReader.close();
					httpURLConnection.disconnect();
					return strResult;
				} else {
					Toast.makeText(getActivity(), 
							"响应错误(─.─|||", Toast.LENGTH_SHORT).show();
					return null;
				}
				
			} catch (java.net.SocketTimeoutException e) {
				// TODO: handle exception
				Toast.makeText(getActivity(), 
						"网络貌似不给力(─.─|||", Toast.LENGTH_SHORT).show();
				Log.v("network", "connectException");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
			
		}
		@Override
		protected void onPostExecute(String strResult) {
			// TODO Auto-generated method stub
			try {
				if (strResult == null) {
					return;
				}
				JSONObject jsonObject = new JSONObject(strResult);
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
	
	public void setEditTextLostFocus() {
		translate_des.setFocusable(true);
		translate_des.setFocusableInTouchMode(true);
		translate_des.requestFocus();
		
	}
	
	public void hideKeyboard() {
		InputMethodManager inputMethodManager = 
				(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(translatInput.getWindowToken(), 
				inputMethodManager.HIDE_NOT_ALWAYS);

	}
	
}
