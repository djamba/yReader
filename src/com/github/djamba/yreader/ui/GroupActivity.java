package com.github.djamba.yreader.ui;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import com.github.djamba.yreader.net.NetLoader;
import com.github.djamba.yreader.rss.Subscriptions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class GroupActivity extends ListActivity {
	
	private String accessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		
		SharedPreferences accessTokenPref = getSharedPreferences("access_token", MODE_PRIVATE);
		this.accessToken = accessTokenPref.getString("access_token", null);
		
		if (this.accessToken == null) {
			Intent intent = new Intent(GroupActivity.this, WebViewActivity.class);
	        startActivity(intent);
		}
		else {
			Log.v("yReader", "OAuth complete, token: [" + this.accessToken + "].");
            Toast.makeText(GroupActivity.this, "Token: " + this.accessToken, Toast.LENGTH_SHORT).show();
		}
		
		final GroupActivity groupActivity = this;
		final ListView listView = (ListView)findViewById(R.id.webview);
		
		new AsyncTask<Object, Object, Object>() {
			
			private String result;
			
			@Override
			protected Object doInBackground(Object... params) {
				
				HttpPost requestInit = new HttpPost("http://api-lenta.yandex.ru/initiate");
				requestInit.addHeader("Content-type", "application/x-yandex-lenta+xml; type=initiate; charset=utf-8");
				requestInit.addHeader("Authorization", "OAuth " + accessToken);
				
				this.result = NetLoader.Load(requestInit).toString();
				Log.v("yReader", "Initiate: [" + this.result + "]");
				
				
				/*HttpGet request = new HttpGet("http://api-lenta.yandex.ru/subscriptions/groups/all");
				request.addHeader("Content-type", "application/x-yandex-lenta+xml; type=group; charset=utf-8");
				request.addHeader("Authorization", "OAuth " + accessToken);*/
				
				/*HttpGet request = new HttpGet("http://api-lenta.yandex.ru/posts?group_id=all&items_per_page=3&read_status=unread");
				request.addHeader("Content-type", "application/x-yandex-lenta+xml; type=posts; charset=utf-8");
				request.addHeader("Authorization", "OAuth " + accessToken);*/
				
				HttpGet request = new HttpGet("http://api-lenta.yandex.ru/subscriptions");
				request.addHeader("Content-type", "application/x-yandex-lenta+xml; type=group; charset=utf-8");
				request.addHeader("Authorization", "OAuth " + accessToken);
				
				this.result = NetLoader.Load(request).toString();
				
				
				return null;
			}
			
			@Override
		    protected void onPostExecute(Object result) {
		     
		        super.onPostExecute(result);
		        
		        Log.v("yReader", "Groups: [" + this.result + "]");
		        
		        Subscriptions subscriptions = new Subscriptions(this.result.toString());
		        
		        ArrayAdapter<Subscriptions.Group> adapter = new ArrayAdapter<Subscriptions.Group>(groupActivity, android.R.layout.simple_list_item_1, subscriptions.getGroups());
		        groupActivity.setListAdapter(adapter);
		    }
			
		}.execute();
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
		
		SharedPreferences accessTokenPref = getSharedPreferences("access_token", MODE_PRIVATE);
		this.accessToken = accessTokenPref.getString("access_token", null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.group, menu);
		return true;
	}

}
