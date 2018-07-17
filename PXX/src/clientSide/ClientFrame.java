package clientSide;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Zaheer Ebrahim
 * Student Number : 216001758
 * Computer Science Final Network Project
 *
 */
public class ClientFrame extends JFrame
{
	/**
	 *   Set 1 : Login Tab components
	 */
	private static final long serialVersionUID = 1L;          
	private JButton btnLogin = new JButton("Login");
    private JButton btnLogout = new JButton("Logout");
    private JButton btnRegister = new JButton("Register");
    private JLabel lblPassword = new JLabel("Password");
    private JLabel lblUsername = new JLabel("Username");
    private JPasswordField txtPassword = new JPasswordField(10);
    private JTextField txtUsername = new JTextField(10);
    private JPanel pnlMainLogin = new JPanel();
    private boolean LoggedIn = false;
	
    /**
     * 	 Set 2 : Image processor Tab components
     */
	private JSlider slider = new JSlider(JSlider.HORIZONTAL,1,255,100);  //Slider for transparency, max value is 255 because of RBG code 100 is transparency	
    private JButton btnOpen = new JButton("Select new image");                       //Buttons
    private JButton btnSave = new JButton("Save masterpiece");
    private JButton btnRotate = new JButton("       Rotate Image       ");    
    private JButton btnAdd = new JButton("  Add 3 more images  ");
    private JButton btnOverlay = new JButton("Save overlay of all 4");
    private JButton btnGray = new JButton("          Grayscale          ");
    private JButton btnInverse = new JButton("            Inverse             ");
    private JLabel lblPic = new JLabel();								//Label for displaying picture 1  
    private JLabel lblPic1 = new JLabel();                                                          //2
    private JLabel lblPic2 = new JLabel();                                                          //3
    private JLabel lblPic3 = new JLabel();                                                          //4
    private JLabel lblHue = new JLabel("Choose a Hue colour");
    private JLabel lblIntensity = new JLabel("      Adjust Intensity");
    private JLabel lblMain = new JLabel("PHOTO EDITIOR");
    private JPanel pnlLogin = new JPanel();                             //Panels
    private JPanel Home = new JPanel();    
    private JPanel grid = new JPanel();
    private JComboBox<String> cmbColourHue = new JComboBox<String>();   //ComboBox for dye colour    
    private ImageIcon currentImageIcon = null;                          //currentIcon 
    private BufferedImage currentBuffImage = null;                      //currentBufferImage
    private static BufferedImage saveBuffImage = null;                  //Image with all processing for saving
    private static Integer degree = 0;                                  //Represents degree of rotation increments by 90 
    private Integer transparency = 100;                                 //represents dye transparency/intensity    
    private JTabbedPane tab = new JTabbedPane();    
    private Boolean multi = false;    
    private BufferedImage b1 = null,b2 = null,b3 = null, s1 = null, s2 = null, s3 = null;;  //Current and Saved buffered images
    private ImageIcon c1 =null, c2=null,c3=null;    
    private BufferedImage origional = null;
    
    /**
     *   Set 3 : Puzzle Tab Components
     */
    JPanel Jiggygrid = new JPanel();     //actual grid
	JPanel pnlInput = new JPanel(); //bottom display / buttons
	JPanel pnlView = new JPanel();  //view grid
	JPanel pnlText = new JPanel();  //panel for top text
	JPanel pnlgrid = new JPanel();	//center panel of grid
	private JLabel lbl1 = new JLabel();
	private JLabel lbl2,lbl3,lbl4,lbl5,lbl6,lbl7,lbl8,lbl9;	//labels for the 9 segments
	private JLabel lblPuzzle = new JLabel("             JIGSAW PUZZLE"); 
	private JLabel lblAnswer = new JLabel("ANSWER                    ");
	private JButton btnCheck = new JButton("Check");   
	private JButton btnReset = new JButton("Reset");
	private JButton btnRules = new JButton("Rules");
	private JLabel lblOrder = new JLabel();
	private int[] currentOrder = null;                //Random order from 1-9 of the images
	private int[] choosenOrder = new int[9];          //Users choosen sequence
	private String answer = "";
	private Integer choosenCount = -1;				  //Counter for num answers user gave
	private BufferedImage bImage = null;
	private JPanel pnlJigSaw = new JPanel();
	private JPanel pnlInfo = new JPanel();
	private Integer ran =0;                          //number of times run
   
    
    /**
     * Frame constructor creates gui's and has listeners
     * @param client
     */
    public ClientFrame(Client client)
    {
    	try {
			client.connect();          
			JOptionPane.showMessageDialog(ClientFrame.this, "Connection to ZX Successful");        //Connect to server
		} catch (IOException e) {
			JOptionPane.showMessageDialog(ClientFrame.this, "Start Sever First");
			System.exit(-1);
		}
    	
    	GUI();                                                                                     //Start main Gui
    	LoggedIn = false;
    	ran = 0;
    	 
    	
    	btnLogin.addActionListener(new ActionListener(){                                           //Log into server                                  

			@Override
			public void actionPerformed(ActionEvent a) {
				//try to login
				String userName = txtUsername.getText().trim();
				String password = txtPassword.getText().trim();
				if(validateText(userName,password))
				{
					txtUsername.setText("");
					txtPassword.setText("");
					String response = client.login(userName, password);                            //Send to server
					JOptionPane.showMessageDialog(ClientFrame.this, response);
					LoggedIn = true;
				}else
					JOptionPane.showMessageDialog(ClientFrame.this, "Please enter username and password");	 //Error message		
			}
    		
    	});
    	
    	btnRegister.addActionListener(new ActionListener(){                                         //Register new user with server

			@Override
			public void actionPerformed(ActionEvent a) {
				//try to login
				String userName = txtUsername.getText().trim();
				String password = txtPassword.getText().trim();
				validateText(userName,password);
				if(validateText(userName,password))
				{
					String response = client.register(userName, password);
					JOptionPane.showMessageDialog(ClientFrame.this, response);
				}else
					JOptionPane.showMessageDialog(ClientFrame.this, "Please enter username and password");
			}
    		
    	});
    	
    	btnLogout.addActionListener(new ActionListener(){                                       //Logoff from server

			@Override
			public void actionPerformed(ActionEvent a) {
				
				String response = client.logout();
				JOptionPane.showMessageDialog(ClientFrame.this, response);
				LoggedIn = false;
			}
    		
    	});   
    	
    	btnOpen.addActionListener(new ActionListener(){              //Opens file for adding image 1 onto the image processor

			@Override
			public void actionPerformed(ActionEvent a) {
				
				ran++;
				if(ran > 1)
				{
					btnAdd.setVisible(false);
					grid.removeAll();
			    	initGui();                                        //Initialize image processor Gui
				}
				JFileChooser file = new JFileChooser();
				file.setCurrentDirectory(new File(System.getProperty("user.home")));  //Navigate to users home
																										//Allowed types
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png","jpeg","gif");
				file.addChoosableFileFilter(filter);
				int result = file.showOpenDialog(null);
				File selectedFile = file.getSelectedFile();
			    String path = selectedFile.getAbsolutePath();
			    String[] tokens = path.split("\\.(?=[^\\.]+$)");                        //Get the extention
			    
			    if((tokens[1].equals("jpg"))||(tokens[1].equals("png"))||(tokens[1].equals("jpeg"))||(tokens[1].equals("gif")))   //make sure its an image
			    {
					BufferedImage defaultImg = null;
					if(result == JFileChooser.APPROVE_OPTION){
						try {
							currentBuffImage = ImageIO.read(new File(path));          //Set current image to user choosen image
							saveBuffImage = currentBuffImage; 
							origional = currentBuffImage;
							defaultImg = ImageIO.read(new File("data/assets/default.jpg")); 
						} catch (IOException e) {
							e.printStackTrace();
						}   													//Convert to icon, scale and display
				    	Image i00 = currentBuffImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
				    	Image iDefault = defaultImg.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
				    	ImageIcon icDefault = new ImageIcon(iDefault);
				    	currentImageIcon = new ImageIcon(i00);
				    	lblPic.setIcon(currentImageIcon);                       //display user choosen image
				    	JLabel lblDefault = new JLabel();
				    	lblDefault.setIcon(icDefault); 
				    	
				    	grid.add(lblDefault);
				    	btnSave.setVisible(true);
				    	btnRotate.setVisible(true);
				    	cmbColourHue.setVisible(true);
				    	slider.setVisible(true);                  //show components
				    	lblHue.setVisible(true);
				    	lblIntensity.setVisible(true);
				    	lblMain.setVisible(true);
				    	btnAdd.setVisible(true);
				    	btnGray.setVisible(true);
				    	btnInverse.setVisible(true);
				    	resizeProcessor();
					}
			    }else
			    {
			    	JOptionPane.showMessageDialog(ClientFrame.this, "Please select an image file");   //error message
			    }
				
			
			}
    	});
    	
    	btnAdd.addActionListener(new ActionListener(){            									//Adds 3 new files

			@Override
			public void actionPerformed(ActionEvent a) {
				
				JFileChooser file = new JFileChooser();
				file.setMultiSelectionEnabled(true);				
				file.setCurrentDirectory(new File(System.getProperty("user.home")));  //Navigate to users home
				int result = file.showOpenDialog(null);																						//Allowed types
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png","jpeg","gif");
				file.addChoosableFileFilter(filter);
				File [] files = file.getSelectedFiles();
				
				if(files.length == 3)
				{
					String path1 = files[0].getAbsolutePath();
					String[] tokens0 = path1.split("\\.(?=[^\\.]+$)");              //Get the pictures extention
					String path2 = files[1].getAbsolutePath();
					String[] tokens1 = path2.split("\\.(?=[^\\.]+$)");
					String path3 = files[2].getAbsolutePath();
					String[] tokens2 = path3.split("\\.(?=[^\\.]+$)");
					
					if(((tokens0[1].equals("jpg"))||(tokens0[1].equals("png"))||(tokens0[1].equals("jpeg"))||(tokens0[1].equals("gif")))&&((tokens1[1].equals("jpg"))||(tokens1[1].equals("png"))||(tokens1[1].equals("jpeg"))||(tokens1[1].equals("gif")))&&((tokens2[1].equals("jpg"))||(tokens2[1].equals("png"))||(tokens2[1].equals("jpeg"))||(tokens2[1].equals("gif"))))
					{
						 path1 = files[0].getAbsolutePath();
						 path2 = files[1].getAbsolutePath();
						 path3 = files[2].getAbsolutePath();				
						
						if(result == JFileChooser.APPROVE_OPTION){
						    
							try {
								 b1 = ImageIO.read(new File(path1));                  //get new images
								 s1 = b1;
								 b2 = ImageIO.read(new File(path2));
								 s2 = b2;
								 b3 = ImageIO.read(new File(path3));
								 s3 = b3;
							} catch (IOException e) {
								e.printStackTrace();
							}							
							 													//Convert to icon, scale and display
					    	Image i00 = b1.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
					    	c1 = new ImageIcon(i00);
					    	Image i01 = b2.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
					    	c2 = new ImageIcon(i01);
					    	Image i02 = b3.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
					    	c3 = new ImageIcon(i02);
					    	
					    	grid.remove(1);                                          //remove default image
					    	
					    	currentBuffImage = origional;                           //show origional image 1
					    	Image origionalImage = origional.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
					    	ImageIcon origionalIcon = new ImageIcon(origionalImage);					    	
					    	lblPic.setIcon(origionalIcon);
					    	
					    	
					    	lblPic1.setIcon(c1);
					    	grid.add(lblPic1);
					    	
					    	lblPic2.setIcon(c2);
					    	grid.add(lblPic2);         //add new images
					    	
					    	lblPic3.setIcon(c3);
					    	grid.add(lblPic3);
					    	
					    	multi = true;              //multi says if all 4 pictures need to be considered
					    	btnAdd.setVisible(false);
					    	btnOverlay.setVisible(true);
					    	
					    	increase();                 //increase dimensions
						}
					}
					else
					{
						JOptionPane.showMessageDialog(ClientFrame.this, "Please select image files only");
					}
					
				}else
				{
					JOptionPane.showMessageDialog(ClientFrame.this, "Please select 3 image files");
				}			
			}
    	});
    	
    	btnRotate.addActionListener(new ActionListener(){            //Rotates file

			@Override
			public void actionPerformed(ActionEvent a) {
				
				degree += 90;                                         //Increments of 90
				BufferedImage rotateBuff = rotate(saveBuffImage); //Convert and display
				Image rotateImage = rotateBuff.getScaledInstance(500, 500, Image.SCALE_SMOOTH);				
		    	ImageIcon rotateIcon = new ImageIcon(rotateImage);
				lblPic.setIcon(rotateIcon);
				saveBuffImage = rotateBuff;
				
				if(multi == true)
				{
					BufferedImage r1 = rotate(s1); //Convert and display and rotate
					Image r11 = r1.getScaledInstance(500, 500, Image.SCALE_SMOOTH);				
			    	ImageIcon r111 = new ImageIcon(r11);
					lblPic1.setIcon(r111);
					s1 = r1;
					
					BufferedImage r2 = rotate(s2); //Convert and display and rotate
					Image r22 = r2.getScaledInstance(500, 500, Image.SCALE_SMOOTH);				
			    	ImageIcon r222 = new ImageIcon(r22);
					lblPic2.setIcon(r222);
					s2 = r2;
					
					BufferedImage r3 = rotate(s3); //Convert and display and rotate
					Image r33 = r3.getScaledInstance(500, 500, Image.SCALE_SMOOTH);				
			    	ImageIcon r333 = new ImageIcon(r33);
					lblPic3.setIcon(r333);
					s3 = r3;
				}				
			}
    	});
   
    	btnGray.addActionListener(new ActionListener(){           //Make image gray using server

			@Override
			public void actionPerformed(ActionEvent a) {
				
				JOptionPane.showMessageDialog(ClientFrame.this, "Please note grayscaling takes a several seconds");
				BufferedImage grayBuff = client.gray(saveBuffImage);                            //Send image to server and get image back
				Image grayImg = grayBuff.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
				ImageIcon grayIcon = new ImageIcon(grayImg);
				lblPic.setIcon(grayIcon);
				saveBuffImage = grayBuff;
				
				if(multi == true)               //if multi send the other 3
				{
					BufferedImage grayBuff1 = client.gray(s1);
					Image grayImg1 = grayBuff1.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
					ImageIcon grayIcon1 = new ImageIcon(grayImg1);
					lblPic1.setIcon(grayIcon1);
					s1 = grayBuff1;

					BufferedImage grayBuff2 = client.gray(s2);     //create scale convert and display
					Image grayImg2 = grayBuff2.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
					ImageIcon grayIcon2 = new ImageIcon(grayImg2);
					lblPic2.setIcon(grayIcon2);
					s2 = grayBuff2;
					
					BufferedImage grayBuff3 = client.gray(s3);
					Image grayImg3 = grayBuff3.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
					ImageIcon grayIcon3 = new ImageIcon(grayImg3);
					lblPic3.setIcon(grayIcon3);
					s3 = grayBuff3;
				}
			}
    	});

    	btnInverse.addActionListener(new ActionListener(){           //create inverse same as gray 

			@Override
			public void actionPerformed(ActionEvent a) {
				
				JOptionPane.showMessageDialog(ClientFrame.this, "Please note inverting takes a several seconds");
				BufferedImage grayBuff = client.inverse(saveBuffImage);
				Image grayImg = grayBuff.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
				ImageIcon grayIcon = new ImageIcon(grayImg);
				lblPic.setIcon(grayIcon);
				saveBuffImage = grayBuff;
				
				if(multi == true)
				{
					BufferedImage grayBuff1 = client.inverse(s1);
					Image grayImg1 = grayBuff1.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
					ImageIcon grayIcon1 = new ImageIcon(grayImg1);
					lblPic1.setIcon(grayIcon1);
					s1 = grayBuff1;

					BufferedImage grayBuff2 = client.inverse(s2);
					Image grayImg2 = grayBuff2.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
					ImageIcon grayIcon2 = new ImageIcon(grayImg2);
					lblPic2.setIcon(grayIcon2);
					s2 = grayBuff2;
					
					BufferedImage grayBuff3 = client.inverse(s3);
					Image grayImg3 = grayBuff3.getScaledInstance(500, 500, Image.SCALE_SMOOTH);	
					ImageIcon grayIcon3 = new ImageIcon(grayImg3);
					lblPic3.setIcon(grayIcon3);
					s3 = grayBuff3;
				}
			}
    	});
    	
    	btnSave.addActionListener(new ActionListener(){              //Save proccesed image to disk

			@Override
			public void actionPerformed(ActionEvent a) {
				                                             //Location
				File outputfile = new File("data/data.png");
				try {
					ImageIO.write(saveBuffImage, "png", outputfile);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				if(multi == true)                 //if multi save other 3
				{
					File outputfile1 = new File("data/data1.png");
					File outputfile2 = new File("data/data2.png");
					File outputfile3 = new File("data/data3.png");
					
					try {
						ImageIO.write(s1, "png", outputfile1);
						ImageIO.write(s2, "png", outputfile2);
						ImageIO.write(s3, "png", outputfile3);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(ClientFrame.this, "Save Success!");
			}
    	});
    	
    	btnOverlay.addActionListener(new ActionListener(){              //Save proccesed image to disk

			@Override
			public void actionPerformed(ActionEvent a) {
				          
				BufferedImage combinedImage = new BufferedImage(486,151,BufferedImage.TYPE_INT_RGB);
				Graphics g = combinedImage.getGraphics();
				
				g.clearRect(0,0, 1000, 1000);  // 1st clear the rect then draw 4 images onto it
				g.drawImage(saveBuffImage, 0, 0, null);
				g.drawImage(s1, 0, 0, null);
				g.drawImage(s2, 0, 0, null);
				g.drawImage(s3, 0, 0, null);
				
				try {                                                                        //save image
					ImageIO.write(combinedImage,"png",new File("data/dataOverlay.png"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}				
				
				JOptionPane.showMessageDialog(ClientFrame.this, "Save Success!");
			}
    	});
    	
    	cmbColourHue.addActionListener (new ActionListener () {      //Change hue based on cmbColourHue
		    public void actionPerformed(ActionEvent e) {
		    	switch(cmbColourHue.getSelectedIndex())
				{
					case 0:
						{   //0 is blue therefore lbl is receiving a blue version of the picture with the users transparency level same for each case
							lblPic.setIcon(getDyePic(new Color(0,9,255,transparency),1));
							if(multi == true)                                              //if multi hue other 3
							{
								lblPic1.setIcon(getDyePic(new Color(0,9,255,transparency),2));
								lblPic2.setIcon(getDyePic(new Color(0,9,255,transparency),3));
								lblPic3.setIcon(getDyePic(new Color(0,9,255,transparency),4));
							}                                                //transparency is set by the user with the slider
							break;
						}			
					case 1:                         
					{
						lblPic.setIcon(getDyePic(new Color(153,76,0,transparency),1));
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(153,76,0,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(153,76,0,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(153,76,0,transparency),4));
						}
						break;
					}		
					case 2:
					{
						lblPic.setIcon(getDyePic(new Color(218,165,32,transparency),1));	
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(218,165,32,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(218,165,32,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(218,165,32,transparency),4));
						}
						break;
					}		
					case 3:
					{
						lblPic.setIcon(getDyePic(new Color(0,255,0,transparency),1));	
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(0,255,0,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(0,255,0,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(0,255,0,transparency),4));
						}
						break;
					}		
					case 4:
					{
						lblPic.setIcon(getDyePic(new Color(255,128,0,transparency),1));	
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(255,128,0,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(255,128,0,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(255,128,0,transparency),4));
						}
						break;
					}		
					case 5:
					{
						lblPic.setIcon(getDyePic(new Color(255,153,255,transparency),1));	
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(255,153,255,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(255,153,255,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(255,153,255,transparency),4));
						}
						break;
					}		
					case 6:
					{
						lblPic.setIcon(getDyePic(new Color(153,0,76,transparency),1));
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(153,0,76,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(153,0,76,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(153,0,76,transparency),4));
						}
						break;
					}		
					case 7:
					{
						lblPic.setIcon(getDyePic(new Color(255,0,0,transparency),1));	
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(255,0,0,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(255,0,0,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(255,0,0,transparency),4));
						}
						break;
					}		
					case 8:
					{
						lblPic.setIcon(getDyePic(new Color(255,255,0,transparency),1));	
						if(multi == true)
						{
							lblPic1.setIcon(getDyePic(new Color(255,255,0,transparency),2));
							lblPic2.setIcon(getDyePic(new Color(255,255,0,transparency),3));
							lblPic3.setIcon(getDyePic(new Color(255,255,0,transparency),4));
						}
						break;
					}		
					default:
						break;
				}
		    }
		});
		
        slider.addChangeListener(new ChangeListener() {              //Changes transparency
           public void stateChanged(ChangeEvent e) {
        	   transparency = slider.getValue();
        	   cmbColourHue.setSelectedIndex(cmbColourHue.getSelectedIndex()); //Run Hue in real time
           }
        });
    	
        tab.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
            	
               if((LoggedIn == true)||(tab.getSelectedIndex() == 0))   //make sure user is logged in before using rest of program
               {
            	   if(tab.getSelectedIndex() == 2)                     //when on Jigsaw puzzle 
                   {
                   	try {
   					    Jiggygrid.removeAll();
   						pnlView.removeAll();                           //start new JigsawPuzzle
   						currentOrder = null;
   						for(int k = 0; k < 9; k++)
   						{
   							choosenOrder[k] = 0;
   						}
   						answer = "";
   						choosenCount = -1;
   						fragmentFile();
   						init();
   					} catch (IOException e1) {
   						e1.printStackTrace();
   					}
                   	resizeJigsaw(315,410);
                   	lbl1.addMouseListener(new MouseAdapter()           //set new mouse listners for cliking an image
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(1);   //sends the value of 1 to show the first image was selected etc for below
               		    } 
               		}); 
               		
               		lbl2.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(2);
               		    } 
               		}); 
               		
               		lbl3.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(3);
               		    } 
               		}); 
               		
               		lbl4.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(4);
               		    } 
               		}); 
               			
               		lbl5.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(5);
               		    } 
               		}); 
               		
               		lbl6.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(6);
               		    } 
               		}); 
               		
               		lbl7.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(7);
               		    } 
               		}); 
               		
               		lbl8.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(8);
               		    } 
               		}); 
               		
               		lbl9.addMouseListener(new MouseAdapter()  
               		{  
               		    public void mouseClicked(MouseEvent e)  
               		    {  
               		    	mouseClick(9);
               		    } 
               		}); 
                   }
                   if(tab.getSelectedIndex() == 1)
                   {
                   	resizeProcessor();
                   }
               }else
               {
            	   tab.setSelectedIndex(0);;                                                    //error message
            	   JOptionPane.showMessageDialog(ClientFrame.this, "Please log in or register first!");
               }
            }
        });
        
        btnRules.addActionListener(new ActionListener(){    //Rules of game

			@Override
			public void actionPerformed(ActionEvent a) {
				
				String welcome = "Welcome to the JIGSAW, select picture in order, from the left, until you have got the correct JIGSAW!";
				JOptionPane.showMessageDialog(ClientFrame.this, welcome);
			}
    		
    	});  
		
		btnReset.addActionListener(new ActionListener(){    //Resets current game

			@Override
			public void actionPerformed(ActionEvent a) {
				
				if(choosenCount == 8)
				{
					reset();
				}else
				{
					JOptionPane.showMessageDialog(ClientFrame.this, "Please finish current puzzle first");
				}
			}
    		
    	});  
		
		btnCheck.addActionListener(new ActionListener(){    //Checks users answer against correct sequence
  
			@Override
			public void actionPerformed(ActionEvent a) {
				
				String UserAns = lblOrder.getText();
				UserAns = UserAns.trim();			

				if(answer.trim().equals("123456789"))
				{
					JOptionPane.showMessageDialog(ClientFrame.this, "Correct");   //if correct reset game
					answer = "";
					reset();
				}
				else
				{
					JOptionPane.showMessageDialog(ClientFrame.this, "Incorrect");
				}
				
			}
    		
    	}); 

		lbl1.addMouseListener(new MouseAdapter()                       //add mouse listeners
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(1);   //sends the value of 1 to show the first image was selected etc for below
		    } 
		}); 
		
		lbl2.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(2);
		    } 
		}); 
		
		lbl3.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(3);
		    } 
		}); 
		
		lbl4.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(4);
		    } 
		}); 
			
		lbl5.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(5);
		    } 
		}); 
		
		lbl6.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(6);
		    } 
		}); 
		
		lbl7.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(7);
		    } 
		}); 
		
		lbl8.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(8);
		    } 
		}); 
		
		lbl9.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {  
		    	mouseClick(9);
		    } 
		}); 
    }
    
    
    
    /**
     * @param color  the colour hue choosen in the combobox
     * @param k  representing the picture number
     * @return AN iMAGEiCON
     */
    private ImageIcon getDyePic(Color color, Integer k)          //Receive colour from mouse, get dyedpic, return dyedpic
	{
    	ImageIcon dyeIcon = null, oldDyeIcon = null;
    	Image dyeImage = null, test = null;
    	BufferedImage dyeBuff = null;
    	switch(k)
    	{
    	case 1:
    		oldDyeIcon = new ImageIcon(dyePic(currentBuffImage, color));    //for each image dye it 
    		break;
    	case 2:
    		oldDyeIcon = new ImageIcon(dyePic(b1, color));
    		break;
    	case 3:
    		oldDyeIcon = new ImageIcon(dyePic(b2, color));
    		break;
    	case 4:
    		oldDyeIcon = new ImageIcon(dyePic(b3, color));
    		break;
    	default:
    		break;
    	}
    	dyeImage = oldDyeIcon.getImage();
		dyeBuff = (BufferedImage) dyeImage;
		switch(k)
    	{
    	case 1:
    		saveBuffImage = dyeBuff;
    		break;
    	case 2:                                                               //refresh the save image so that more processes can be added
    		s1 = dyeBuff;
    		break;
    	case 3:
    		s2 = dyeBuff;
    		break;
    	case 4:
    		s3 = dyeBuff;
    		break;
    	default:
    		break;
    	}
		test = dyeBuff.getScaledInstance(500, 500, Image.SCALE_SMOOTH);				
		dyeIcon = new ImageIcon(test);
    	return dyeIcon;

	} 
 
   
    /**
     * @param image the image sent by the above function
     * @param color the colour chooser from the combobox
     * @return the dyed bufferedimage
     */
    private static BufferedImage dyePic(BufferedImage image, Color color)   //Receive img and colour, change hue and dye picture and return it
    {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage dyed = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);  //dye image
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0,0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0,0,w,h);
        g.dispose();
        return dyed;
    }
    
  
    /**
     * @param stock which is the stock image before roation
     * @return  new rotated image
     */
    private static BufferedImage rotate(BufferedImage stock)                //Rotate image using stock image to rotate it
    {
    	AffineTransform at = AffineTransform.getRotateInstance(Math.toRadians(degree), (int)(stock.getWidth()/2), (int)(stock.getHeight()/2));       
        BufferedImage newRotated = new BufferedImage(stock.getWidth(), stock.getHeight(), stock.getType()); 
        saveBuffImage = newRotated;		                                   //save the rotation																														
        Graphics2D g = (Graphics2D) newRotated.getGraphics();                                                                                           
        g.setTransform(at);                                                                                                                         
        g.drawImage(stock, 0, 0, null);                                                                                                                                  
        g.dispose();
        stock.flush();
        return newRotated;    	
    }
   
   
    /**
     *   main gui
     */
    private void GUI()
    {
    	this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    	this.setLayout(new BorderLayout());    	
    	this.setSize(1368,770);
    	this.setTitle("Zx Image Processor"); 
    	
    	try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	initGui();
    	
    	JLabel lblHeading = new JLabel("                                        Zx Image Processor");
    	lblHeading.setFont(new Font("Veranda", Font.BOLD, 25));
    	lblHeading.setForeground(Color.WHITE);
    	lblUsername.setForeground(Color.WHITE);
    	lblPassword.setForeground(Color.WHITE);
    	txtUsername.setBackground(Color.WHITE);
    	txtPassword.setBackground(Color.WHITE);
    	btnLogin.setBackground(Color.WHITE);
    	btnRegister.setBackground(Color.WHITE);            //add componets and layouts to add tabs and login
    	btnLogout.setBackground(Color.WHITE);
    	
    	pnlMainLogin.setLayout(new FlowLayout());   
    	pnlMainLogin.add(new JLabel(" "));
    	pnlMainLogin.add(new JLabel(" "));
    	pnlMainLogin.add(new JLabel(" "));
    	pnlMainLogin.add(lblUsername); 
    	pnlMainLogin.add(txtUsername);
    	pnlMainLogin.add(lblPassword);
    	pnlMainLogin.add(txtPassword);
    	pnlMainLogin.add(btnLogin);
    	pnlMainLogin.add(btnRegister);
    	pnlMainLogin.add(btnLogout);
    	pnlMainLogin.setBackground(Color.BLACK);
    	
    	JPanel pnlHeading = new JPanel();    	
    	pnlHeading.setBackground(Color.BLACK);
    	pnlHeading.setLayout(new BorderLayout());
    	pnlHeading.add(lblHeading, BorderLayout.NORTH);
    	pnlHeading.add(pnlMainLogin, BorderLayout.CENTER);
    	
    	tab.addTab("Welcome", pnlHeading);
    	tab.addTab("Image Processor", Home);
    	tab.addTab("Puzzle", pnlInfo);
    	tab.setBackground(Color.WHITE);
    	tab.setForeground(Color.BLACK);
    	
    	this.add(tab);
    	this.setBackground(Color.BLACK);
    	this.setLocationRelativeTo(null);
    }
    
   
    /**
     *  gui initalisation for the image processor
     */
    private void initGui()  //start gui
    {   	
    	pnlLogin.setLayout(new BoxLayout(pnlLogin, BoxLayout.Y_AXIS));
    	btnSave.setVisible(false);
    	cmbColourHue.setVisible(false);
    	slider.setVisible(false);
    	btnRotate.setVisible(false);
    	lblHue.setFont(new Font("Veranda", Font.BOLD,12));
    	lblIntensity.setFont(new Font("Veranda", Font.BOLD,12));
    	lblMain.setFont(new Font("Veranda", Font.BOLD,25));
    	lblHue.setVisible(false);
    	lblIntensity.setVisible(false);
    	lblMain.setVisible(false);
    	btnAdd.setVisible(false);        //add componets to process image and open and save the images
    	btnOverlay.setVisible(false);
    	btnGray.setVisible(false);
    	btnInverse.setVisible(false);
    	
    	cmbColourHue.addItem("Blue");
    	cmbColourHue.addItem("Brown");
    	cmbColourHue.addItem("Gold");
    	cmbColourHue.addItem("Green");
    	cmbColourHue.addItem("Orange");    	
    	cmbColourHue.addItem("Pink");
    	cmbColourHue.addItem("Purple");
    	cmbColourHue.addItem("Red");
    	cmbColourHue.addItem("Yellow");
    	
    	cmbColourHue.setMaximumSize(new Dimension(450,25));
    	cmbColourHue.setBackground(Color.WHITE);
    	btnOpen.setBackground(Color.WHITE);
    	slider.setBackground(Color.BLACK);
    	slider.setForeground(Color.WHITE);
    	btnSave.setBackground(Color.WHITE);
    	btnRotate.setBackground(Color.WHITE);
    	lblIntensity.setForeground(Color.WHITE);
    	lblMain.setForeground(Color.WHITE);
    	btnOverlay.setBackground(Color.WHITE);
    	btnGray.setBackground(Color.WHITE);
    	btnInverse.setBackground(Color.WHITE);
    	txtUsername.setBackground(Color.WHITE);
    	txtPassword.setBackground(Color.WHITE);
    	
    	lblHue.setForeground(Color.WHITE);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(lblMain);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(btnOpen);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(lblHue);
    	pnlLogin.add(cmbColourHue);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(lblIntensity);
    	pnlLogin.add(slider);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(btnRotate);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(btnGray);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(btnInverse);
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(new JLabel(" "));
    	pnlLogin.add(btnAdd);
    	pnlLogin.setBackground(Color.BLACK);
    	
    	JPanel pnlTry = new JPanel();
    	pnlTry.setBackground(Color.BLACK);
    	pnlTry.setLayout(new BorderLayout());
    	pnlTry.add(pnlLogin, BorderLayout.CENTER);
    	
    	JPanel pnlSave = new JPanel();
    	pnlSave.setLayout(new FlowLayout());
    	pnlSave.add(btnSave);
    	pnlSave.add(btnOverlay);
    	pnlSave.setBackground(Color.BLACK);
    	
    	grid.setLayout(new GridLayout(2,2));
    	grid.setBackground(Color.BLACK);
    	grid.add(lblPic); 
    	
    	Home.setBackground(Color.BLACK);
    	Home.setLayout(new BorderLayout());
    	Home.add(pnlTry, BorderLayout.CENTER);
    	Home.add(grid, BorderLayout.WEST);
    	Home.add(pnlSave,BorderLayout.SOUTH);    	
    	
    }
    
 
    /**
     *    Make the frame full size for the 4 image processor
     */
    private void increase()
    {
    	this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
  
   
    /**
     * @param k representing the current image index selected to get the users order
     */
    private void lblOrder(String k) {
		
		lblOrder.setText(lblOrder.getText()+" "+k);
	} 
	
    
	/**
	 *  Add 9 picture segments to grid for the puzzle
	 */
	private void addPics()                           //adds labels to grid using random array
	{
		Jiggygrid.setLayout(new GridLayout(3,3));
		Jiggygrid.setBackground(Color.BLACK);
		currentOrder = new Random().ints(1, 10).distinct().limit(9).toArray(); //Create new random array to display 9 segmnets in a random order
		for(int k = 0; k < 9; k++)
    	{
    		switch(currentOrder[k])
        	{
    		case 1: Jiggygrid.add(lbl1);
    				break;
    		case 2: Jiggygrid.add(lbl2);
					break;
    		case 3: Jiggygrid.add(lbl3);
					break;
    		case 4: Jiggygrid.add(lbl4);
					break;
    		case 5: Jiggygrid.add(lbl5);
					break;
    		case 6: Jiggygrid.add(lbl6);
					break;
    		case 7: Jiggygrid.add(lbl7);
					break;
    		case 8: Jiggygrid.add(lbl8);
					break;
    		case 9: Jiggygrid.add(lbl9);
					break;
			default : break;        	
        	}
    	}
		
		pnlgrid.setLayout(new BorderLayout());
		pnlgrid.add(Jiggygrid, BorderLayout.CENTER);
		
		pnlJigSaw.add(pnlgrid, BorderLayout.WEST);
		
	}
	

	/**
	 *  reset puzzle for a new puzzle
	 */
	private void reset()
	{
		lblAnswer.setVisible(false);
		addPics();
		choosenCount = -1;
		lblOrder.setText("");
		answer = "";
		for(int k = 0; k < 9; k++)
		{
			choosenOrder[k] = 0;
		}
		resizeJigsaw(315,410);
	}
	
	
    /**
     *     add the user genrated answer grid to the right
     */
    private void addView()                           //adds user selected labels to the answer grid
	{
		pnlView.setLayout(new GridLayout(3,3));
		pnlView.setBackground(Color.BLACK);
		for(int k = 0; k < 9; k++)
    	{
    		switch(choosenOrder[k])
        	{
    		case 1: pnlView.add(lbl1);
    				break;
    		case 2: pnlView.add(lbl2);
					break;
    		case 3: pnlView.add(lbl3);
					break;
    		case 4: pnlView.add(lbl4);
					break;
    		case 5: pnlView.add(lbl5);
					break;
    		case 6: pnlView.add(lbl6);
					break;
    		case 7: pnlView.add(lbl7);
					break;
    		case 8: pnlView.add(lbl8);
					break;
    		case 9: pnlView.add(lbl9);
					break;
			default : break;        	
        	}
    	}
		pnlJigSaw.add(pnlView, BorderLayout.EAST);
		resizeJigsaw(800,410);

	}

	
    /**
     * @param currentInstance which picture was selectd 1 --> 9
     */
    private void mouseClick(Integer currentInstance)
	{
		lblAnswer.setVisible(true);
		 choosenCount++;
	       for(int k = 0; k < 9; k++)
	       {
	    	   if(currentOrder[k] == currentInstance)    //current order is compared to 123456789  if for eg the 1st piece is in the 5th position the user will select
	    	   {										 //the 5th postion and the choosenOrder will record [  1  ] not [  5  ]
	    		   lblOrder(Integer.toString(k+1));
	    		   choosenOrder[choosenCount] = currentInstance;
	    	   }
	       }
	       answer += Integer.toString(currentInstance);	       
	       addView();                                      //Remove the image and put on the view grid on the right
	}
	

	/**
	 * Function to crop picture to break into 9 segemnts for the puzzle game
	 * @throws IOException
	 */
	private void fragmentFile() throws IOException
	{
		boolean bSelected = false;
		
		while(bSelected == false)
		{
			JFileChooser file = new JFileChooser();
			file.setCurrentDirectory(new File(System.getProperty("user.home")));  //Navigate to users home
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png","jpeg","gif");//Allowed types
			file.addChoosableFileFilter(filter);
			int result = file.showSaveDialog(null);
			File selectedFile = file.getSelectedFile();
		    String path = selectedFile.getAbsolutePath();                 //get image and check extention
		    String[] tokens = path.split("\\.(?=[^\\.]+$)");
		    
		    if((tokens[1].equals("jpg"))||(tokens[1].equals("png"))||(tokens[1].equals("jpeg"))||(tokens[1].equals("gif")))
		    {
				
		    	if(result == JFileChooser.APPROVE_OPTION){
				    selectedFile = file.getSelectedFile();
				    path = selectedFile.getAbsolutePath();				   
					try {
						bImage = ImageIO.read(new File(path));                      //Set current image to user choosen image
					} catch (IOException e) {
						e.printStackTrace();
					}   													
				}
				
		    	int width = bImage.getWidth();
				int height = bImage.getHeight();
				int fragmentWidth = width / 3;    // divide main picture into 3 to get segments of width or height
				int fragmentHeight = height / 3;
				int x = 0;
				int y = 0;
				int z = 0;                        //counter to name the pictures 1 -> 9
				
		    	
				for(int k = 0; k < 3; k++)   								     	 // each column
				{
					y = 0;                										    //go back to col 0
					
					for(int j = 0; j < 3; j++) 									    //each row
					{
					    z++;                                                        //create fragmented image
					    
					    System.out.println("Run fn"+z);
						BufferedImage fragmentImage =  bImage.getSubimage(y, x, fragmentWidth, fragmentHeight);
						File fragmentFile = new File("data/temp/fragment"+z+".png");     //create file for fragmented image with number     
						ImageIO.write(fragmentImage, "png", fragmentFile);          //write image to file						
						y += fragmentWidth;                                         //increase for next fragment column
					}
					x += fragmentHeight;                                            //inc for next fragment row
				}
				bSelected = true;
				
		    }else
		    {
		    	JOptionPane.showMessageDialog(ClientFrame.this, "Please select an image file");
		    	bSelected = false;
		    }
		}		
		
	}
	

	/** initaliser for the puzzle gui
	 * @throws IOException
	 */
	private void init() throws IOException {            //Initalises the basic form

		
		BufferedImage img00 = ImageIO.read(new File("data/temp/fragment1.png"));
		BufferedImage img01 = ImageIO.read(new File("data/temp/fragment2.png")); //add the defualt images for the first puzzle run
		BufferedImage img02 = ImageIO.read(new File("data/temp/fragment3.png"));
		BufferedImage img10 = ImageIO.read(new File("data/temp/fragment4.png"));
		BufferedImage img11 = ImageIO.read(new File("data/temp/fragment5.png"));
		BufferedImage img12 = ImageIO.read(new File("data/temp/fragment6.png"));
		BufferedImage img20 = ImageIO.read(new File("data/temp/fragment7.png"));
		BufferedImage img21 = ImageIO.read(new File("data/temp/fragment8.png"));
		BufferedImage img22 = ImageIO.read(new File("data/temp/fragment9.png"));


		//converting from buffered to image icon (to put in label) and scaling down to 100x100px
		Image i00 = img00.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii00 = new ImageIcon(i00);
		lbl1 = new JLabel(ii00);

		Image i01 = img01.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii01 = new ImageIcon(i01);
		lbl2 = new JLabel(ii01);

		Image i02 = img02.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii02 = new ImageIcon(i02);
		lbl3 = new JLabel(ii02);

		Image i10 = img10.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii10 = new ImageIcon(i10);
		lbl4 = new JLabel(ii10);

		Image i11 = img11.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii11 = new ImageIcon(i11);
		lbl5 = new JLabel(ii11);

		Image i12 = img12.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii12 = new ImageIcon(i12);
		lbl6 = new JLabel(ii12);

		Image i20 = img20.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii20 = new ImageIcon(i20);
		lbl7 = new JLabel(ii20);

		Image i21 = img21.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii21 = new ImageIcon(i21);
		lbl8 = new JLabel(ii21);

		Image i22 = img22.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon ii22 = new ImageIcon(i22);
		lbl9 = new JLabel(ii22);
		//adding componets of the labels and buttons
		pnlText.setLayout(new BorderLayout());
		pnlText.add(lblPuzzle, BorderLayout.WEST);
		pnlText.add(lblAnswer, BorderLayout.EAST);

		pnlInput.setLayout(new FlowLayout());
		pnlInput.add(lblOrder);
		pnlInput.add(btnCheck);
		pnlInput.add(btnReset);
		pnlInput.add(btnRules);

		lblPuzzle.setFont(new Font("Veranda", Font.BOLD,20));
		lblAnswer.setFont(new Font("Veranda", Font.BOLD,20));
		lblOrder.setFont(new Font("Veranda", Font.BOLD,20));
		lblPuzzle.setForeground(Color.WHITE);
		lblAnswer.setForeground(Color.WHITE);
		lblOrder.setForeground(Color.WHITE);    	
		btnCheck.setBackground(Color.WHITE);                     //Add components
		btnReset.setBackground(Color.WHITE);
		btnRules.setBackground(Color.WHITE);
		lblAnswer.setVisible(false);
		pnlJigSaw.setLayout(new BorderLayout());
		pnlJigSaw.setBackground(Color.BLACK);
		
		addPics();	
		addView();

		pnlText.setBackground(Color.BLACK);
		pnlInput.setBackground(Color.BLACK);		
		
		
		pnlInfo.setLayout(new BorderLayout());
		pnlInfo.setBackground(Color.BLACK);		
		
		
		pnlInfo.add(pnlText, BorderLayout.PAGE_START);
		pnlInfo.add(pnlInput, BorderLayout.PAGE_END);
		pnlInfo.add(pnlJigSaw, BorderLayout.CENTER);
	}
	
	
	/**  resize the puzzle given width and height 
	 * @param width
	 * @param height
	 */
	private void resizeJigsaw(Integer width,Integer height)
	{
		this.setSize(width,height);
		this.setLocationRelativeTo(null);
	}
	
	
	/** 
	 *   resize the image processor to 1 image
	 */
	private void resizeProcessor()
	{
		this.setSize(800, 770);
		this.setLocationRelativeTo(null);
		this.pack();
	}

	
	/** Validate the username and password to make sure they arent empty
	 * @param username
	 * @param password
	 * @return
	 */
	private boolean validateText(String user,String pass)
	{
		if((user.equals(""))||(pass.equals("")))
		{
			return false;
		}
		return true;    	
	}
	
}






