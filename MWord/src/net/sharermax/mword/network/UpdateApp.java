package net.sharermax.mword.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.string;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

public class UpdateApp {

	public static final String BETA_INFO_URL = "http://mword.qiniudn.com/version%2Fbeta_version.json";
	public static final String RELEASE_INFO_URL = "http://mword.qiniudn.com/version%2Frelease_version.json";
	public static final String APP_RELEASE = "";
	public static final String APP_BETA = "";
	public static final String APP_BETA_DOWNLOAD = "http://mword.qiniudn.com/app%2FMWord_Beta.apk";
	public static final String APP_RELEASE_DOWNLOAD = "http://mword.qiniudn.com/app%2FMWord.apk";
	
	public static final int VERSION_TYPE_BETA = 0;
	public static final int VERSION_TYPE_RELEASE = 1;
	
	private int mVersionCode;
	private String mVersionName;
	private Context mContext;
	private int mVersionType;
	private TaskOverListener mTaskOverListener;
	public UpdateApp(Context context, int versionType) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mVersionType = versionType;
		
	}
	
	public void startTask() {
		Log.v("UPDATE", "Start");
		GetVersionTask getVersionTask = new GetVersionTask();
		if (mVersionType == VERSION_TYPE_BETA) {
			getVersionTask.execute(BETA_INFO_URL);
		} else if (mVersionType == VERSION_TYPE_RELEASE) {
			getVersionTask.execute(RELEASE_INFO_URL);
		}
	}
	
//	public boolean hasUpdate() throws NameNotFoundException {
//		PackageManager packageManager = mContext.getPackageManager();
//		PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
//		Log.v("UPDATE", "" + mVersionCode);
//		if (mVersionCode > packageInfo.versionCode) {
//			
//			return true;
//		}
//		return false;
//	}
	
	class GetVersionTask extends AsyncTask<String, Integer, String> {
		
		@Override
		protected String doInBackground(String... versionStrings) {
			// TODO Auto-generated method stub
			try {
				URL versionUrl = new URL(versionStrings[0]);
				HttpURLConnection httpURLConnection = 
						(HttpURLConnection)versionUrl.openConnection();
				httpURLConnection.setConnectTimeout(3000);
				httpURLConnection.setDoInput(true);
				httpURLConnection.setRequestMethod("GET");
				
				if (httpURLConnection.getResponseCode() == HttpStatus.SC_OK) {
					InputStream versionInputStream = httpURLConnection.getInputStream();
					String versionInfo = "";
					String lineString = null;
					BufferedReader versionBufferedReader = new BufferedReader(
							new InputStreamReader(versionInputStream));
					while ((lineString = versionBufferedReader.readLine()) != null) {
						versionInfo += lineString;						
					}
					versionBufferedReader.close();
					versionInputStream.close();
					httpURLConnection.disconnect();
					return versionInfo;
				} else {
//					Toast.makeText(getActivity(), 
//							"响应错误(─.─|||", Toast.LENGTH_SHORT).show();
					return null;
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String versionInfo) {
			// TODO Auto-generated method stub
			super.onPostExecute(versionInfo);
			if (versionInfo == null) {
				return;
			}
			try {
				Log.v("UPDATE", versionInfo);
				JSONObject jsonObject = new JSONObject(versionInfo);
				mVersionCode = jsonObject.getInt("version_code");
//				mVersionName = jsonObject.getString("version_name");
				Log.v("UPDATE", "" + mVersionCode);
				if (mTaskOverListener != null) {
					mTaskOverListener.taskOver(mVersionCode);
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void setTaskOverListener(TaskOverListener taskOverListener) {
		mTaskOverListener = taskOverListener;
	}
	
	public static interface TaskOverListener{
		public abstract void taskOver(int versionCode);
	}
}
