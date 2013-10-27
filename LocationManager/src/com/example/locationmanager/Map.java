package com.example.locationmanager;



import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class Map extends Activity{

	/*Reference to the google map object */
	private GoogleMap map;
	private Spinner spinner;
	

	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setUpSpinner();
        
        List<User> l = MainActivity.mUsers;
        
	     // Get a handle to the Map Fragment
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
	    map.setMyLocationEnabled(false);
	    map.setTrafficEnabled(false);
	    map.setIndoorEnabled(false);
	    
	    setMapOnClick(l);
	    
	    for(int i = 0; i < l.size(); i++){
			List<GPSMarkerImage> markerImages = l.get(i).getImages();
			for(int j = 0; j < markerImages.size(); j++){
				//Getting the first image
				Bitmap image = markerImages.get(j).getImage();
				if(image != null){//
					map.addMarker(new MarkerOptions()
		            .title(markerImages.get(j).getTitle())
		            .snippet(markerImages.get(j).getSnippet())
		            .position(markerImages.get(j).getLatLng())
		            .icon(BitmapDescriptorFactory.fromBitmap(markerImages.get(j).getImage()))
		            .visible(true));
				}
				
				else{//Icon image will not be set if image is null
					map.addMarker(new MarkerOptions()
		            .title(markerImages.get(j).getTitle())
		            .snippet(markerImages.get(j).getSnippet())
		            .position(markerImages.get(j).getLatLng())
		            .visible(true));
				}
			}
		}
	    
	    if(l.size() > 0){
	    	map.moveCamera(CameraUpdateFactory.newLatLngZoom(
					l.get(0).getImages().get(0).getLatLng(), 17));
	    }
    
    }
	
	/**
	 * This method sets up the OnTemSelected listener of the spinner.
	 * This gives user a way to change the map type to normal, satellite, or terrain
	 */
	private void setUpSpinner(){
		//Setting up the spinner
        spinner = (Spinner) findViewById(R.id.spinner);
    	List<String> list = new ArrayList<String>();
    	list.add("Normal"); //Three Types of maps available
    	list.add("Satelite");
    	list.add("Terrain");
    	
    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    	dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	spinner.setAdapter(dataAdapter);
    	
    	spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    	    @Override
    	    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
    	        if(position == 0)
    	        	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	        else if(position == 1)
    	        	map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    	        else
    	        	map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    	    }

    	    @Override
    	    public void onNothingSelected(AdapterView<?> parentView) {
    	    	map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	    }

    	});
	}
	
	/**
	 * This method sets the click listener on the map. It then displays a
	 * dialog box which allows the user to look at available users on the server.
	 * @param list
	 */
	private void setMapOnClick(final List<User> list){
		map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){
			@Override
			public void onMapLongClick(LatLng point) {
				AlertDialog.Builder alertDialogBuilder = 
						new AlertDialog.Builder(Map.this);
				
				//Setting the title
				alertDialogBuilder.setTitle("Choose user name");
				
				final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                    Map.this,
	                    android.R.layout.select_dialog_singlechoice);
				
				for(int i = 0; i < list.size(); i++)
					arrayAdapter.add(list.get(i).getUserName());
				
				alertDialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						map.moveCamera(CameraUpdateFactory.newLatLngZoom(
								list.get(which).getImages().get(0).getLatLng(), 17));
					}
					
				});
				
				alertDialogBuilder.create().show();
			}
			
		});
	}
}