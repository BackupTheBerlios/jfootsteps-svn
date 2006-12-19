package trunk;

/*
 * Main.java
 *
 * Created on 15. Dezember 2006, 10:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author Stephe
 */
public class Main {
    
	
    /** Creates a new instance of Main */
    public Main() {}
       
       
    public static void main(String[] args) {
        
    	String url = "C:\\DATEN\\Java workspace\\footsteps2\\trunk\\trumpet.xml";
    	
    	XMLParse xml = new XMLParse(url);
    		
    	System.out.println("Path: " + xml.getPath());
    	System.out.println("Start: " + xml.getStart());
    	System.out.println("Duration: " + xml.getDuration());
    	System.out.println("Volume: " + xml.getVolume());
    	System.out.println("Pitch: " + xml.getPitch());	
    	System.out.println("Speed: " + xml.getSpeed());
    	
    	
    	
        	
        /*Footsteps fs = new Footsteps();
        try
        {
            System.out.println("opening...");
            fs.open();
            System.out.println("done.");
        }
        catch(Exception ex) {}
        }*/
   
    }
}
    
