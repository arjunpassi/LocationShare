package edu.vt.ece4564.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This servlet
 * @author Arjun Passi
 *
 */
public class DataServlet extends HttpServlet {

	/**
	 * The user does HTTP post on the server with 7 parameters
	 * user name, password, image, longitude, latitude, title, and snippet.
	 * Even if the parameters are null the server will return the users 
	 * on the server.
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	resp.setContentType( "JSON" );
    	
    	String username = req.getParameter("username");
    	String password = req.getParameter("password");
    	String image = req.getParameter("image");
    	String lon = req.getParameter("longitude");
    	String lat = req.getParameter("latitude");
    	String title = req.getParameter("title");
    	String snippet = req.getParameter("snippet");
    	
    	//Hash this user and see if its present
    	User user = new User(username, password, lon, lat, image, title, snippet);
    	
    	//Reference to the users on the server
    	HashMap<String, User> users = MyFirstServlet.users;
    	
    	//Checking if the username is present and the password passed is correct
    	if(users.get(username) != null){
    		System.out.println("Username: " + username);
    		System.out.println("Password: " + password);
    		if(users.get(username).getPassword().equals(password)){
    			users.remove(username);
    	    	users.put(username, user);
    		}
    	}
    	
    	//Getting a array list from hash map
    	ArrayList<User> list = Collections.list(Collections.enumeration(users.values()));
    	JSONArray jsonArray = new JSONArray();
    	
    	//Traversing the list and creating Json objects
    	for(int i = 0; i < list.size(); i++){
    		User user_ = list.get(i);
			JSONObject obj = new JSONObject();
			try {
				obj.put("username", user_.getUserName());
				obj.put("password", user_.getPassword());
				obj.put("longitude", user_.getLongitude());
				obj.put("latitude", user_.getLatitude());
				obj.put("title", user_.getTitle());
				obj.put("snippet", user_.getSnippet());
				obj.put("image", user_.getImage());
				
				System.out.println(obj);
				
				jsonArray.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	
    	
		resp.getWriter().write(jsonArray.toString());
    }
}
