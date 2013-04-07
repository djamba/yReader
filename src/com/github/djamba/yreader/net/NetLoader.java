package com.github.djamba.yreader.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class NetLoader {
	
	    String strResult;
	    
	    public static StringBuilder Load(HttpUriRequest request) {
	     
	    	StringBuilder strBuilder = new StringBuilder();

	    	try {
				
	    		HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				
				BufferedReader bufReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				
				for (String s = null; (s = bufReader.readLine()) != null;) {
					strBuilder.append(s);
				}
	    	}
	    	catch (IOException e) {
	    		Log.e("NetLoader::Load", e.getMessage());
	    	}

			return strBuilder;
	    }
}
