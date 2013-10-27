package com.example.locationmanager;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a User.
 * @author Arjun Passi
 *
 */
public class User {

	/*Reference to the user name of this user */
	private String mUserName;
	
	/*References to the password of this user */
	private String mPassword;
	
	/*Reference to the images this person took */
	/*In this version I will probably store only one image in this list */
	private List<GPSMarkerImage> mImages;
	
	/**
	 * Constructs a User object
	 * @param username
	 * @param password
	 * @param images
	 */
	User(String username, String password, List<GPSMarkerImage> images){
		mUserName = username;
		mPassword = password;
		
		if(images == null)
			mImages = new ArrayList<GPSMarkerImage>();
		else
			mImages = images;
	}
	
	/**
	 * Constructs the User object
	 * @param username
	 * @param password
	 */
	User(String username, String password){
		mUserName = username;
		mPassword = password;
		mImages = new ArrayList<GPSMarkerImage>();
	}
	
	/**
	 * Adds a GPSMarkerImage to the user
	 * @param image
	 */
	public void addGPSMarkerImage(GPSMarkerImage image){
		mImages.add(image);
	}
	
	/**
	 * @return list of GPSMarkerImages
	 */
	public List<GPSMarkerImage> getImages(){
		return mImages;
	}
	
	/**
	 * @return user name
	 */
	public String getUserName(){
		return mUserName;
	}
	
	/**
	 * @return password
	 */
	public String getPassword(){
		return mPassword;
	}
	
	public String toString(){
		StringBuilder build = new StringBuilder();
		build.append("Username: " + mUserName.toString());
		build.append("Password: " + mPassword.toString());
		build.append("GPSMarker: " + mImages.toString());
		return build.toString();
	}
}
