package com.example.locationmanager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

/**
 * GPSImageResponseParser class is responsible for parsing
 * the Json response recieved from the server.
 * @author arjun
 *
 */
public class GPSImageResponseParser {

	/*Reference to the string that contains the server's response when 
	  an image is requested*/
	private String mServerResponse;
	
	/**
	 * Constructor for the GPSImageResponseParser
	 * @param serverResponse
	 */
	public GPSImageResponseParser(String serverResponse){
		mServerResponse = serverResponse;
	}
	
	/**
	 * This method parses the Json response from the server
	 * and returns a list of users
	 * @return
	 */
	public List<User> getUsers(){
		
		List<User> list = new ArrayList<User>();
		
		try {
			JSONArray jsonArray = new JSONArray(mServerResponse);
			
			//Traversing the list and parsing the Json response
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject obj = jsonArray.getJSONObject(i);
				
				String username = obj.getString("username");
				String password = obj.getString("password");
				String image = obj.getString("image");
				String longitude = obj.getString("longitude");
				String latitude = obj.getString("latitude");
				String title = obj.getString("title");
				String snippet = obj.getString("snippet");
				
				byte [] raw = Base64.decode(image.getBytes(), 0);
				
				List<GPSMarkerImage> markerImageList = new ArrayList<GPSMarkerImage>();
				GPSMarkerImage markerImage = new GPSMarkerImage(raw, longitude, latitude, title, snippet);
				markerImageList.add(markerImage);
				User user = new User(username, password, markerImageList);
				list.add(user);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
