package com.example.locationmanager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogInActivity extends Activity{
	
	/*Reference to the LogIn async task */
	private LogInQuery login_;
	
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        login_ = new LogInQuery(this);
        
        //Setting up the login button with a listener
        setUpLogInButton();
	}
	
	private void setUpLogInButton(){
		
		Button logIn_ = (Button) findViewById(R.id.logIn);
        logIn_.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(login_.getStatus() == AsyncTask.Status.RUNNING)
					return;
				login_ = null;
				login_ = new LogInQuery(LogInActivity.this);
				login_.execute((Void) null);
			}
        	
        });
	}
	
	/**
	 * @return user name
	 */
	public String getUserName(){
		EditText editText = (EditText) findViewById(R.id.username);
		return editText.getText().toString();
	}
	
	/**
	 * @return password
	 */
	public String getPassword(){
		EditText editText = (EditText) findViewById(R.id.password);
		return editText.getText().toString();
	}
	
	/**
	 * @return IP and port number
	 */
	public String getIpPort(){
		EditText editText = (EditText) findViewById(R.id.ip_port);
		return editText.getText().toString();
	}
	
	@SuppressLint("NewApi")
	public void changeIntent(String result){
		//If result is null or empty then server was not found
		if(result == null || result.isEmpty()){
			Context context = getApplicationContext();
			CharSequence text = "Server not found! Check your IP and Port";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			return;
		}
		//If the HTML contained unsuccessful then user name
		//or password was incorrect
		else if(result.contains("unsuccessful")){
			Context context = getApplicationContext();
			CharSequence text = "Incorrect username or password";
			int duration = Toast.LENGTH_SHORT;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			return;
		}
		
		Intent intent = new Intent(this, MainActivity.class );
		intent.putExtra("username", getUserName());
		intent.putExtra("password", getPassword());
		intent.putExtra("IP_Port", getIpPort());
		this.startActivity(intent);
	}

}
