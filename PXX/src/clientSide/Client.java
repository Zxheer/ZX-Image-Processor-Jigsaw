package clientSide;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Zaheer Ebrahim
 * Student Number : 216001758
 * Computer Science Final Network Project
 *
 */
public class Client {                             
	
	private static final int PORT = 2017;
	private static final String HOST = "localhost";	
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private InputStream inStream = null;
	private OutputStream outStream = null;

	public static void main(String[] args) {
		
		Client client = new Client();                       //Creating client and frame instance
		ClientFrame frame = new ClientFrame(client);
		frame.setVisible(true);		
		try {
		  	UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");   //Using UIManager Nimbus
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		

	}

    /** try connect to the server and bind the streams
     * @throws IOException
     */
    public void connect() throws IOException
    {
		socket = new Socket(HOST, PORT);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		inStream = socket.getInputStream();
		outStream = socket.getOutputStream();
    }
    
    /** Recieve login from user and send to server to try login
     * @param username
     * @param password
     * @return
     */
    public String login(String username, String password)
    {
    	String response = "";
    	out.println("LOGIN " + username + " " + password);
    	try {
			response = in.readLine();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	
    	return response;
    }
    
    /** Logout from server
     * @return
     */
    public String logout()
    {
    	String response = "";
    	out.println("LOGOFF");
    	try {
			response = in.readLine();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	
    	return response;
    }
    
    /** Recieve login from user and send to server to try login
     * @param username
     * @param password
     * @return
     */
    public String register(String username, String password)
    {
    	String response = "";
    	out.println("REG " + username + " " + password);
    	try {
			response = in.readLine();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	
    	return response;
    }
	
    /** Inverses image from client by server and returned to client
     * @param stock repersenting the stock image that will be inverted
     * @return
     */
    public BufferedImage inverse(BufferedImage stock)
    {
    	BufferedImage recieveImg = null;
    	String response = "";
    	out.println("INVERT");   
    	try
    	{
    		response = in.readLine();              //check if server says +OK
    		String cmd = response.substring(0,1);
    		
    		if(cmd.equals("+"))
    		{
    			ImageIO.write(stock, "PNG", outStream);    //send the image      		
        		recieveImg = ImageIO.read(inStream);       //Receive processed image
        		response = in.readLine();                  //get server final confirmation of sent
        		System.out.println(response);
        		
    		}else
    		{
    			response = "Could not invert image";   //if server unavailable or some error 
    		} 		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return recieveImg;
    }

    /** Grayscales image from client by server and returned to client
     * @param stock representing the stock image that will be inverted
     * @return
     */
    public BufferedImage gray(BufferedImage stock)
    {
    	BufferedImage recieveImg = null;
    	String response = "";
    	out.println("GRAY");   
    	try
    	{
    		response = in.readLine();                    //check if server says +OK
    		String cmd = response.substring(0,1);
    		
    		if(cmd.equals("+"))
    		{   
        		ImageIO.write(stock, "PNG", outStream);  //send the image      		
        		recieveImg = ImageIO.read(inStream);      //Receive processed image
        		response = in.readLine();                 //get server final confirmation of sent
        		System.out.println(response);             
        		
    		}else
    		{
    			response = "Could not grayscale image";      //if server unavailable or some error 
    		} 		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return recieveImg; 	
    }

    /** Crops an image sent from the client by the server
     * @param stock  the original image for cropping
     * @param x   the starting x coord
     * @param y   the starting y coord
     * @param w   the width of the requested sub image
     * @param h   the height of the requested sub image
     * @return
     */
    public String crop(BufferedImage stock, int x, int y, int w, int h)
    {
    	BufferedImage recieveImg = null;
    	String response = "";
    	out.println("CROP "+x+" "+y+" "+w+" "+h); 
    	try
    	{
    		response = in.readLine();
    		String cmd = response.substring(0,1);
    		
    		if(cmd.equals("+"))
    		{    
        		ImageIO.write(stock, "PNG", outStream);        //send image
        		
        		File img = new File("Client/data/cropped.png");  //create new file for saving		
        		recieveImg = ImageIO.read(inStream);             //recieve image
        		ImageIO.write(recieveImg, "PNG", img);           //write recieved image to the file
			
        		response = in.readLine();
    		}else
    		{
    			response = "Could not crop image";
    		} 		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return response; 	                                   //return response to client
    }
}
