package com.example.locationmanager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

public class GPSImageQuery extends AsyncTask<Void, Void, List<User>>{

	/*Reference to the user that will be posted on the server */
	private User mUser;
	
	/*Reference to the main activity */
	private MainActivity mActivity;
	
	/*Reference to the url that the users data will be posted to */
	private String url = "";
	
	/**
	 * Constructor to the GPSImageQuery
	 * @param user
	 * @param ip_addr
	 */
	public GPSImageQuery(User user, MainActivity act_){
		mUser = user;
		mActivity = act_;
		url = mActivity.getIpPort();
		
		if(!url.substring(0, 6).contains("http://"))
			url = "http://" + url;
		
		url = url + "/data";
	}
	
	/**
	 * This method posts users current information of the user
	 * and returns the current information of all the users on the
	 * server
	 * @return
	 * @throws Exception
	 */
	public ArrayList<User> userInquery() throws Exception {
		
		if(mUser == null)
			return new ArrayList<User>();
		
		BufferedReader in = null;
		String data = null;
		
		String usr = mUser.getUserName();
		String pass = mUser.getPassword();
		
		try {
			// setup HTTP client
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			
			//This version will also support user have one image on the server
			if(mUser.getImages() == null || mUser.getImages().size() == 0)
				return new ArrayList<User>();
			
			double longitude = mUser.getImages().get(0).getLatLng().longitude;
			double latitude = mUser.getImages().get(0).getLatLng().latitude;
			
			//Converting the values to string
			String lon = String.valueOf(longitude);
			String lat = String.valueOf(latitude);
			
			GPSMarkerImage markerImage = mUser.getImages().get(0);
			Bitmap image = markerImage.getImage();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 100, out);
			byte[] imageInByteArray = out.toByteArray();
			//String image_string = new String(imageInByteArray);
			//String image_string = imageInByteArray.toString();
			String image_string = Base64.encodeToString(imageInByteArray, Base64.DEFAULT);
			
			String title = markerImage.getTitle();
			String snippet = markerImage.getSnippet();
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			
			//Going to post user name as a parameter
			nameValuePairs.add(new BasicNameValuePair("username", usr)); 
			
			//Going to post password as a parameter
			nameValuePairs.add(new BasicNameValuePair("password", pass)); 
			
			//Going to post the image as a parameter
			nameValuePairs.add(new BasicNameValuePair("image", image_string));
			
			//Going to post the longitude as a parameter
			nameValuePairs.add(new BasicNameValuePair("longitude", lon)); 
			
			//Going to post latitude as a parameter
			nameValuePairs.add(new BasicNameValuePair("latitude", lat)); 
			
			//Going to post the title as the parameter
			nameValuePairs.add(new BasicNameValuePair("title", title)); 
			
			//Going to post the snippet as the parameter
			nameValuePairs.add(new BasicNameValuePair("snippet", snippet)); 
			
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
			
			GPSImageResponseParser parser = new GPSImageResponseParser(data);
			return (ArrayList<User>) parser.getUsers();
			
		} finally {
			if ( in != null ){
				try {
					in.close();
					GPSImageResponseParser parser = new GPSImageResponseParser(data);
					return (ArrayList<User>) parser.getUsers();
				} catch ( Exception e ) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	protected List<User> doInBackground(Void... params) {
		try {
			return userInquery();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<User>(0);
		}
	}
	
	@Override
	protected void onPostExecute( List<User> result ) {
		mActivity.dataFromImageQuery(result);
	}

}
