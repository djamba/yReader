package com.github.djamba.yreader.net;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebClient extends WebViewClient {
	
	private ArrayList<OnPageStarted> listPageStartedListener;
	
	@Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
		
        view.loadUrl(url);
        return true;
    }
    
	@Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
		
		this.sendPageStartedListener(view, url, favicon);
    }
	
	public void addPageStartedListener(OnPageStarted listner) {
		
		if (this.listPageStartedListener == null) {
			this.listPageStartedListener = new ArrayList<OnPageStarted>();
		}
		
		this.listPageStartedListener.add(listner);
	}
	
	public void removePageStartedListener(OnPageStarted listner) {
		
		if (this.listPageStartedListener == null) {
			
			int idx = this.listPageStartedListener.indexOf(listner);
			
			if (idx > 0) {
				this.listPageStartedListener.remove(idx);
			}
		}
	}
	
	private void sendPageStartedListener(WebView view, String url, Bitmap favicon) {
		
		if (this.listPageStartedListener != null) {
			
			final ArrayList<OnPageStarted> list = this.listPageStartedListener;
			final int count = list.size();
			
			for (int i = 0; i < count; i++) {
				list.get(i).onPageStarted(view, url, favicon);
			}
		}
	}
	
	public interface OnPageStarted {
		
		void onPageStarted(WebView view, String url, Bitmap favicon);
	}

}
