import javax.media.*;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.format.AudioFormat;
import javax.media.protocol.ContentDescriptor;


public class AudioExtractor implements ControllerListener, DataSinkListener {

	Processor p = null;
	DataSink ds = null;
	String inFile, outFile;
	
	public AudioExtractor(){}
	
	public AudioExtractor (String inputFile, String outputFile){
		inFile = inputFile;
		outFile = outputFile;
		init();
	}
	
	public void init () {
		
		try {
			p = Manager.createProcessor(new MediaLocator(inFile));
			p.addControllerListener(this);
			p.configure();
		} catch (Exception e) {}
	}
	
	public synchronized void dataSinkUpdate (DataSinkEvent event) {
		if (event instanceof DataSinkErrorEvent) {
			System.out.println("Data sink exception" + ((DataSinkErrorEvent)event).toString());
		}
		
	}
	
	public synchronized void controllerUpdate (ControllerEvent event) {
		if (event instanceof ConfigureCompleteEvent) {
			AudioFormat outFormat = new AudioFormat(AudioFormat.MPEG);
			TrackControl tracks[] = p.getTrackControls();
			boolean found = false;
			
			for (int i=0; i<tracks.length; i++) {
				Format trackFormat = tracks[i].getFormat();
				
				if(!found && trackFormat instanceof AudioFormat) {
					Codec[] audioConversion = new Codec[] {
							new com.sun.media.codec.audio.mpa.NativeEncoder()
					};
					
					try {
						tracks[i].setCodecChain(audioConversion);
						tracks[i].setFormat(outFormat);
						p.setContentDescriptor(new ContentDescriptor("audio.mpeg"));
					} catch (Exception e) {}
					
					found = true;
					
				} else {
					tracks[i].setEnabled(false);
				}
			}
			
			if (found) p.realize();
			
		} else if (event instanceof RealizeCompleteEvent) {
			try{
				ds = Manager.createDataSink(p.getDataOutput(),new MediaLocator(outFile));
				ds.addDataSinkListener(this);
				ds.open();
				ds.start();
				p.start();
			} catch (Exception e) {}
			
		} else if (event instanceof EndOfMediaEvent) {
			p.stop();
			p.deallocate();
			try {
				ds.stop();
			} catch (Exception e) {}
			
			ds.close();
		}
	}
}
