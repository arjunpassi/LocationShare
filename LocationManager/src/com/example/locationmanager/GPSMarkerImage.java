package com.example.locationmanager;

import com.google.android.gms.maps.model.LatLng;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * GPSMarkerImage represents the image that is being displayed
 * on the map.
 * 
 * @author Arjun Passi
 *
 */
public class GPSMarkerImage {

	/*Reference to the image */
	private Bitmap mImage;
	
	/*Reference to the location where the image was taken */
	private LatLng mLocation;
	
	/*Reference to the title of the marker */
	private String mTitle;
	
	/*Reference to the snippet of the maker*/
	private String mSnippet;
	
	/**
	 * Constructor for GPSMarkerImage
	 * @param image
	 * @param location
	 * @param title
	 * @param snippet
	 */
	GPSMarkerImage(Bitmap image, LatLng location, String title, String snippet){
		mImage = image;
		mLocation = location;
		mTitle = title;
		mSnippet = snippet;
	}
	
	/**
	 * Constructor for GPSMarkerImage
	 * @param image
	 * @param location
	 * @param title
	 * @param snippet
	 */
	GPSMarkerImage(String image, LatLng location, String title, String snippet){
		
		byte [] image_raw = image.getBytes();
		mImage = BitmapFactory.decodeByteArray(image_raw, 0, image_raw.length);
		mLocation = location;
		mTitle = title;
		mSnippet = snippet;
	}
	
	/**
	 * Constructor for GPSMarkerImage
	 * @param image
	 * @param lon
	 * @param lat
	 * @param title
	 * @param snippet
	 */
	GPSMarkerImage(String image, String lon, String lat, String title, String snippet){
		
		byte [] image_raw = image.getBytes();
		mImage = BitmapFactory.decodeByteArray(image_raw, 0, image_raw.length);
		mLocation = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
		mTitle = title;
		mSnippet = snippet;
	}
	
	/**
	 * Constructor for GPSMarkerImage
	 * @param image
	 * @param lon
	 * @param lat
	 * @param title
	 * @param snippet
	 */
	GPSMarkerImage(byte[] image, String lon, String lat, String title, String snippet){
		mImage = BitmapFactory.decodeByteArray(image, 0, image.length);
		mLocation = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
		mTitle = title;
		mSnippet = snippet;
	}
	
	/**
	 * Constructor for GPSMarkerImage
	 * @param image
	 * @param lon
	 * @param lat
	 * @param title
	 * @param snippet
	 */
	GPSMarkerImage(Bitmap image, String lon, String lat, String title, String snippet){
		mImage = image;
		mLocation = new LatLng(Double.valueOf(lat), Double.valueOf(lon));
		mTitle = title;
		mSnippet = snippet;
	}
	
	/**
	 * @return bit map image
	 */
	public Bitmap getImage(){
		return mImage;
	}
	
	/**
	 * @return LatLng where the image was taken
	 */
	public LatLng getLatLng(){
		return mLocation;
	}
	
	/**
	 * @return title of the image
	 */
	public String getTitle(){
		return mTitle;
	}
	
	/**
	 * @return snippet of the image
	 */
	public String getSnippet(){
		return mSnippet;
	}
	
	public String toString(){
		StringBuilder build = new StringBuilder();
		build.append("Image: " + mImage.toString());
		build.append("LatLng: " + mLocation.toString());
		build.append("Title: " + mTitle.toString());
		build.append("Snippet: " + mSnippet.toString());
		return build.toString();
	}
}
