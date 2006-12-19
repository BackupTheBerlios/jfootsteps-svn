package trunk;

import javax.xml.parsers.*;
import org.xml.sax.helpers.*;
import org.xml.sax.*;

public class SaxHandler extends DefaultHandler {
	
	public void startDocument() throws SAXException {
	}
	
	public void endDocument() throws SAXException {
	}
	
	public void characters (char[] ch, int start, int length) throws SAXException {
	}
	
	public void startElement (String nameSpaceURI, String localName, String qName, Attributes atts) throws SAXException {
				
		/**Reading Data for all used elements to Strings*/
		
		if (qName == "effectsettings") {
		
		} else if(qName == "path") {
			
			String pathStr = new String();
			
			for (int i = 0; i<atts.getLength(); i++) {
				pathStr += atts.getValue(i);
			}
			XMLParse.setPath(pathStr);
		
		} else if (qName == "start") {
			
			String startStr = new String();
			
			for (int i = 0; i<atts.getLength(); i++) {
				startStr += atts.getValue(i);
			}
			XMLParse.setStart(Integer.parseInt(startStr));
		
		} else if (qName == "duration") {
			
			String durationStr = new String();
			
			for (int i = 0; i<atts.getLength(); i++) {
				durationStr += atts.getValue(i);
			}
			XMLParse.setDuration(Integer.parseInt(durationStr));
		
		} else if (qName == "volume") {
			
			String volumeStr = new String();
			
			for (int i = 0; i<atts.getLength(); i++) {
				volumeStr += atts.getValue(i);
			}
			volumeStr += "F";
			XMLParse.setVolume(Float.parseFloat(volumeStr));
		
		} else if (qName == "pitch") {
			
			String pitchStr = new String();
			
			for (int i = 0; i<atts.getLength(); i++) {
				pitchStr += atts.getValue(i);
			}
			pitchStr += "F";
			XMLParse.setPitch(Float.parseFloat(pitchStr));
		
		} else if (qName == "speed") {
			
			String speedStr = new String();
			
			for (int i = 0; i<atts.getLength(); i++) {
				speedStr += atts.getValue(i);
			}
			speedStr += "F";
			XMLParse.setSpeed(Float.parseFloat(speedStr));
		
		} else {
			
			System.out.println("Failure in XML-File!!!");
			return;
		}	
	}
	
	public void endElement (String namespaceURI, String localName, String qName) throws SAXException {
	}
}
