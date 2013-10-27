package edu.vt.ece4564.example;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.server.Server;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;

/**
 * MyFirstServlet class
 * @author arjun
 *
 */
public class MyFirstServlet extends HttpServlet
{
	/*Reference to the users */
	static HashMap<String, User> users = new HashMap<String, User>();
	
    public static void main(String[] args) throws Exception {

        Server server = new Server(8090);
        
        WebAppContext context = new WebAppContext();
        context.setWar("war");
        context.setContextPath("/");
        server.setHandler(context);

        server.start();
        server.join();
    }
    
    @Override 
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { 
    	
    	resp.setContentType( "text/html" );
    	String usr = req.getParameter("username");
    	String pas = req.getParameter("password");
        PrintWriter out = resp.getWriter();

        String response = "Login was successfull.";
        
        //Checking if the user exists and invalid password was not passed
        if(usr == null || pas == null || users.get(usr) != null){
        	//If the password doesn't match
        	if(!users.get(usr).getPassword().equals(pas))
        		response = "Login was unsuccessful.";
        }
        else{ //User is not present create one
        	User user = new User(usr, pas, "0.0", "0.0", "", "", "");
        	users.put(usr, user);
        }
        
        	
        out.println("<html>");
        out.println("<body>");
        out.println(response);
        out.println("</body>");
        out.println("</html>");
        
    }
}
