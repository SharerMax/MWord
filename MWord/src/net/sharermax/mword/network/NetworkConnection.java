package net.sharermax.mword.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkConnection {
	private ConnectivityManager  connectivityManager;
	public NetworkConnection(Context context) {
		// TODO Auto-generated constructor stub
		connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public boolean isConnected() {
		if (connectivityManager.getActiveNetworkInfo() != null) {
			return connectivityManager.getActiveNetworkInfo().isConnected();
		} else {
			return false;
		}
	}
}
