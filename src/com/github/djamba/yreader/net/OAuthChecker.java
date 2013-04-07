package com.github.djamba.yreader.net;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;

public class OAuthChecker implements WebClient.OnPageStarted {
	
	public final static String OAUTH_URL = "https://oauth.yandex.ru/authorize?response_type=token&client_id=a914c1b4c8ed45d7b77e866f3074342f&display=popup";
	
	private String accessToken;
	private ArrayList<OnTokenRecived> listTokenRecivedListener;
	
	public String getAccessToken() {
		
		return this.accessToken;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		
		String fragment = "#access_token=";
        int start = url.indexOf(fragment);
        
        if (start > -1) {
            
        	int end = url.indexOf('&', start);
        	this.accessToken = url.substring(start + fragment.length(), end == -1 ? url.length() : end);

            Log.v("yReader", "OAuth complete, token: [" + this.accessToken + "]");
            
            this.sendTokenRecivedListener(this.accessToken);
        }
	}
	
	public void addTokenRecivedListener(OnTokenRecived listner) {
		
		if (this.listTokenRecivedListener == null) {
			this.listTokenRecivedListener = new ArrayList<OnTokenRecived>();
		}
		
		this.listTokenRecivedListener.add(listner);
	}
	
	public void removeTokenRecivedListener(OnTokenRecived listner) {
		
		if (this.listTokenRecivedListener == null) {
			
			int idx = this.listTokenRecivedListener.indexOf(listner);
			
			if (idx > 0) {
				this.listTokenRecivedListener.remove(idx);
			}
		}
	}
	
	private void sendTokenRecivedListener(String accessToken) {
		
		if (this.listTokenRecivedListener != null) {
			
			final ArrayList<OnTokenRecived> list = this.listTokenRecivedListener;
			final int count = list.size();
			
			for (int i = 0; i < count; i++) {
				list.get(i).onRecived(accessToken);
			}
		}
	}
	
	public interface OnTokenRecived {
		
		void onRecived(String accessToken);
	}

}
