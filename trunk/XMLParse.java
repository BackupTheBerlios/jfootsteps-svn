package trunk;

import java.io.FileReader;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;


public class XMLParse {
	
	private static String path;
	private static int start;
	private static int duration;
	private static float volume;
	private static float pitch;
	private static float speed;
	
	
	public XMLParse (String xmlPath) {		
		effectParser (xmlPath);
	}
	
	
	public static void effectParser (String xmlpath) {
		
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();
			InputSource iS = new InputSource(new FileReader(xmlpath));
			parser.parse(iS, new SaxHandler());			
		} catch (Exception e) {
			System.out.println("Failure: " + e);
		}
	}
	
	/** setter*/
	public static void setPath(String path) {
		XMLParse.path = path;
	}
	
	public static void setStart(int start) {
		XMLParse.start = start;
	}
	
	public static void setDuration(int duration) {
		XMLParse.duration = duration;
	}
	
	public static void setVolume (float volume) {
		XMLParse.volume = volume;
	}
	
	public static void setPitch (float pitch) {
		XMLParse.pitch = pitch;
	}
	
	public static void setSpeed (float speed) {
		XMLParse.speed = speed;
	}
	
	/** getter*/
	public String getPath() {
		return path;
	}
	
	public int getStart() {
		return start;
	}
	public int getDuration() {
		return duration;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getSpeed() {
		return speed;
	}
}
