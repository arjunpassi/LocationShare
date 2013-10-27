package edu.vt.ece4564.example;

import javax.tools.JavaFileManager.Location;

public class User {
	
	/*Reference to the user name */
	private String mUserName;
	
	/*Reference to the password */
	private String mPassword;
	
	/*Reference to the longitude */
	private String mLongitude;
	
	/*Reference to the latitude */
	private String mLatitude;
	
	/*Reference to the image. This needs to be converted to byte[] */
	private String mImageData;
	
	/*Reference to the title of the image */
	private String mTitle;
	
	/*Reference to the snippet of the image*/
	private String mSnippet;
	
	/**
	 * Constructs a user object
	 * @param username
	 * @param password
	 * @param lon
	 * @param lat
	 * @param image
	 * @param title
	 * @param snippet
	 */
	User(String username, String password, String lon, 
			String lat, String image, String title,
			String snippet){
		
		mUserName = username;
		mPassword = password;
		mLongitude = lon;
		mLatitude = lat;
		mImageData = image;
		mTitle = title;
		mSnippet = snippet;
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
	
	/**
	 * @return longitude
	 */
	public String getLongitude(){
		return mLongitude;
	}
	
	/**
	 * @return latitude
	 */
	public String getLatitude(){
		return mLatitude;
	}
	
	/**
	 * @return title
	 */
	public String getTitle(){
		return mTitle;
	}
	
	/**
	 * @return snippet
	 */
	public String getSnippet(){
		return mSnippet;
	}
	
	/**
	 * @return image
	 */
	public String getImage(){
		return mImageData;
	}

}
