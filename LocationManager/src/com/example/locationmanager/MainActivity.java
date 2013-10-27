package com.example.locationmanager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener{

	/*Reference to LocationManager */
    private LocationManager locationManager_;
    
    /*Reference to the GPSImageQuery async task */
    private GPSImageQuery gpsImageQuery;
    
    /*Reference to the user name of the user */
    private String username = "";
    
    /*Reference to the password of the user */
    private String password = "";
    
    /*Reference to the servers IP and port number. Ex: 192.168.1.103:8090 */
    private String IP_Port = "";
    
    /*Reference to the list of users */
    static List<User> mUsers = new ArrayList<User>();
    
    /*Reference to the current latitude */
    private static double latitude;
    
    /*Reference to the current longitude */
    private static double longitude;
    
    /* */
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //mapActivity = (Map) new Activity();
        
        //Intent intent = new Intent(this, Map.class );
		//this.startActivity(intent);
		//overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        
        Intent in = getIntent();
        Bundle bundle = in.getExtras();
        
        
        if(bundle != null){
        	username = bundle.get("username").toString();
        	password = bundle.get("password").toString();
        	IP_Port = bundle.get("IP_Port").toString();
        }
        
        //This might give me a null pointer ..Not taking that in account
        locationManager_ = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager_.removeUpdates(MainActivity.this); 
        
        setSeekBar();
        setUpCheckBoxListener();
        setTakePictureButton();
    }
	
	private void setTakePictureButton(){
		Button button = (Button) findViewById(R.id.takepic);
		
		button.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 0);
			}
		});
	}
	
	private void setSeekBar(){
		
		SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setMax(100);
        
		seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				
				locationManager_.removeUpdates(MainActivity.this); 
				
				if(progress == 0){
					displayMessage("GPS updates changed to 0 ms");
					return;
				}
				
				locationManager_.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, progress, 
				        Criteria.ACCURACY_FINE, MainActivity.this);
				
				displayMessage("GPS updates changed to " + progress + "ms");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {}
        });
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode == 0 && resultCode == RESULT_OK){
			Bitmap image = (Bitmap) data.getExtras().get("data");
			
			List<GPSMarkerImage> markerImageList = new ArrayList<GPSMarkerImage>();
			GPSMarkerImage markerImage = new GPSMarkerImage(image, 
					String.valueOf(longitude), String.valueOf(latitude), "Title", "This is my picture");
			markerImageList.add(markerImage);
			User user = new User(username, password, markerImageList);
			gpsImageQuery = new GPSImageQuery(user, this);
			gpsImageQuery.execute((Void) null);
		}
	}
	
	/**
	 * This method sets up a check box change listener.
	 * Every time the user enables or disables the gps
	 * updated the 
	 */
	private void setUpCheckBoxListener(){
		CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				CharSequence text;
				
				if(isChecked){//Status changed to check..set up location updates
					text = "GPS updates will be posted to the server";
					SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
					int progress = seekbar.getProgress();
					if(progress != 0){
						locationManager_.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, progress, 
						        Criteria.ACCURACY_FINE, MainActivity.this);
					}
				}
				else{//Remove the location updates
					text = "GPS updates will not be posted to the server";
					locationManager_.removeUpdates(MainActivity.this);
				}
				
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			
		});
	}
	
	/**
	 * Check box gives the user a way to choose weather
	 * GPS updates need to be posted to the server or not.
	 * @return the status of the check box
	 */
	public boolean updateGpsLocation(){
		CheckBox checkbox = (CheckBox) findViewById(R.id.checkBox);
		return checkbox.isChecked();
	}
	
	/*
	 * Turns off the gps updates
	 * (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
    protected void onPause() { 
    	super.onPause(); 
    	locationManager_.removeUpdates(this); 
	} 
    
	protected void onResume() { 
    	super.onResume();
    	SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);
    	int progress = seekbar.getProgress();
    	locationManager_.requestLocationUpdates(LocationManager.GPS_PROVIDER, progress, 
    	Criteria.ACCURACY_FINE, this); 	
	}

	@Override
	public void onLocationChanged(Location location) {
		
		longitude = location.getLongitude(); 
		latitude = location.getLatitude();
		
		displayMessage("Current Longitude: " + longitude + '\n' +
					   "Current Latitude " + latitude);
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		
		Context context = getApplicationContext();
		CharSequence text = "GPS provider disabled!";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Context context = getApplicationContext();
		CharSequence text = "GPS provider Enabled! Changes will be posted on the server";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		
		if(status == LocationProvider.OUT_OF_SERVICE){
			CharSequence text = "GPS provider out of service!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else if(status == LocationProvider.TEMPORARILY_UNAVAILABLE){
			CharSequence text = "GPS provider temporarily unavailable!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
		else{
			CharSequence text = "GPS provider available!";
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}
	
	public void setUsers(List<User> l){
		mUsers = l;
	}

	/**
	 * @return user name
	 */
	public String getUserName(){
		return username;
	}
	
	/**
	 * @return password
	 */
	public String getPassword(){
		return password;
	}
	
	/**
	 * @return IP and port number
	 */
	public String getIpPort(){
		return IP_Port;
	}

	/**
	 * This method will update the text view
	 * @param result
	 */
	public void displayMessage(String result) {
		TextView textView = (TextView) findViewById(R.id.textview);
		textView.setText(result);
		
	}
	
	/**
	 * This method is called when the GPSImageQuery is finished
	 * @param list
	 */
	public void dataFromImageQuery(List<User> list){
		
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); 
		v.vibrate(300);
		displayMessage("New data recieved from network");
		mUsers = list;
		
		Intent intent = new Intent(MainActivity.this,  Map.class);
		MainActivity.this.startActivity(intent);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
	}
}