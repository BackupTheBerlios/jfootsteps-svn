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
       
       
        public static void main(String[] args)
        {
             Footsteps fs = new Footsteps();
        try
        {
            System.out.println("opening...");
             fs.open();
            System.out.println("done.");
        }
        catch(Exception ex) {}
        }
   
}
    
