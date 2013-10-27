package com.example.locationmanager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.AsyncTask;

public class LogInQuery extends AsyncTask<Void, Void, String>{

	/*Reference to LogInActivity */
	private LogInActivity mLogInActivity;
	
	/**
	 * Constructor for log in query
	 * @param act_
	 */
	public LogInQuery(LogInActivity act_){
		mLogInActivity = act_;
	}
	
	
public String logIn() throws Exception {
		
		BufferedReader in = null;
		String data = null;
		String url = mLogInActivity.getIpPort();
		
		if(!url.substring(0, 6).contains("http://"))
			url = "http://" + url;
		
		String usr = mLogInActivity.getUserName();
		String pass = mLogInActivity.getPassword();
		
		try {
			// setup HTTP client
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			
			//Going to post user name as a parameter
			nameValuePairs.add(new BasicNameValuePair("username", usr)); 
			
			//Going to post password as a parameter
			nameValuePairs.add(new BasicNameValuePair("password", pass)); 
			
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
			post.setEntity(entity);
			
			HttpResponse response = client.execute(post);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer buffer = new StringBuffer( "" );
			String line = "";
			String newline = System.getProperty( "line.separator" );
			while ( (line = in.readLine()) != null ) {
				buffer.append( line + newline );
			}
			
			in.close();
			data = buffer.toString();
			
			return data;
			
		} finally {
			if ( in != null ){
				try {
					in.close();
					return data;
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			return logIn();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	protected void onPostExecute( String result ) {
		mLogInActivity.changeIntent(result);
	}

}
