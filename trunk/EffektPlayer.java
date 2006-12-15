import javax.media.*;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

//this is a comment

public class EffektPlayer extends Frame implements ControllerListener {
	
	Processor p;
	Object waitSync = new Object();
	boolean stateTransitionOK = true;
	//Player player;
	Component vc, cc;
	boolean first = true, loop = false;
	
	public EffektPlayer (String title) {
		super(title);
	}
	
	public boolean open(MediaLocator m1) {
		
		try {
			p = Manager.createProcessor(m1);
		} catch (Exception e) {
			System.err.println("Failed to create a processor from the given url: " + e);
			return false;
		}
		
		p.addControllerListener(this);
		
		p.configure();
		if (!waitForState(p.Configured)) {
			System.err.println("Failed to configure the processor");
			return false;
		}
		
		p.setContentDescriptor(null);
		
		TrackControl tc[] = p.getTrackControls();
		if (tc == null){
			System.err.println("Failed to obtain track controls form the processor.");
			return false;
		}
		
		TrackControl audioTrack = null;
		for (int i=0; i< tc.length; i++) {
			if(tc[i].getFormat() instanceof AudioFormat) {
				audioTrack = tc[i];
				break;
			}
		}
		
		if(audioTrack == null) {
			System.err.println("The input media doesn't contain a audio Track.");
			return false;
		}
		
		System.err.println("Audio Format: " + audioTrack.getFormat());
		
		try {
			Codec codec[] = {new GainEffect()};
			audioTrack.setCodecChain(codec);
		} catch (UnsupportedPlugInException e) {
			System.err.println("The processor doesn't support effects");
		}
	
		p.prefetch();
		
		
		if(!waitForState(p.Prefetched)){
			System.out.println("Failed to realize teh processor.");
			return false;
		}
		
		
		setLayout(new BorderLayout());
		
		Component cc;
	
		if((cc=p.getControlPanelComponent()) != null) {
			add("South", cc);
		}
		
		p.start();
		
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				p.close();
				System.exit(0);
			}
		});
		
		return true;
	}
	
	public void addNotify() {
		super.addNotify();
		pack();
	}
	
	boolean waitForState(int state) {
		synchronized (waitSync) {
			try {
				while (p.getState() != state && stateTransitionOK)
					waitSync.wait();
			} catch (Exception e) {}
		}
		return stateTransitionOK;
	}
	
	
	public void controllerUpdate (ControllerEvent e) {
		
		if (e instanceof ConfigureCompleteEvent ||
			e instanceof RealizeCompleteEvent ||
			e instanceof PrefetchCompleteEvent) {
				
				stateTransitionOK = true;
				waitSync.notifyAll();
		} else if (e instanceof ResourceUnavailableEvent) {
			synchronized (waitSync) {
				stateTransitionOK = false;
				waitSync.notifyAll();
			}
		} else if (e instanceof EndOfMediaEvent) {
			p.close();
			System.exit(0);
		}
	}
	
	public static void main (String[] args) {
		
		if(args.length == 0) {
			prUsage();
			System.exit(0);
		}
		
		String url = "file:" + "C:\\DATEN\\Java workspace\\jfootsteps\\aufnahme.wav";
		
		if (url.indexOf(":") < 0) {
			prUsage();
			System.exit(0);
		}
		
		MediaLocator m1;
		
		if((m1 = new MediaLocator(url)) == null) {
			System.err.println("Cannot build media locator from: " + url);
			System.exit(0);
		}
		
		EffektPlayer player = new EffektPlayer(url);
		
		if(!player.open(m1)) {
			System.exit(0);
		}
	}
	
	static void prUsage() {
		System.err.println("Usage: java EffektPlayer <url>");
	}
	
}