package com.github.djamba.yreader.ui;

import com.github.djamba.yreader.net.OAuthChecker;
import com.github.djamba.yreader.net.OAuthChecker.OnTokenRecived;
import com.github.djamba.yreader.net.WebClient;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.webkit.WebView;

public class WebViewActivity extends Activity {
	
	private WebView webview;
	private WebClient webClient;
	
	private OAuthChecker oAuthChecker;

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_view);
		
		this.oAuthChecker = new OAuthChecker();
		this.oAuthChecker.addTokenRecivedListener(new OnTokenRecived() {
			
			@Override
			public void onRecived(String accessToken) {
				
				SharedPreferences accessTokenPref = getSharedPreferences("access_token", MODE_PRIVATE);
				SharedPreferences.Editor editor = accessTokenPref.edit();
	            editor.putString("access_token", accessToken);
	            editor.commit();
				
	            finish();
			}
		});
		
		this.webview = (WebView)findViewById(R.id.webview);
		
		this.webClient = new WebClient();
		this.webClient.addPageStartedListener(oAuthChecker);
		
		this.webview.setWebViewClient(this.webClient);
		this.webview.getSettings().setJavaScriptEnabled(true);
        
        this.webview.loadUrl(OAuthChecker.OAUTH_URL);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.web_view, menu);
		return true;
	}

}
