package trunk;


import java.io.IOException;

import javax.media.*;
import javax.media.format.*;
import javax.media.protocol.PullBufferDataSource;
import javax.media.protocol.PullBufferStream;
import javax.media.protocol.PullDataSource;
import javax.media.protocol.PullSourceStream;


import com.sun.media.protocol.file.DataSource;


public class Footsteps implements Effect {
	
	private static String effectName = "Footsteps";		//Name
	protected AudioFormat inputFormat;					//inputformat
	protected AudioFormat outputFormat;					//outputformat
	protected Format[] supportedInFormats = new Format[0];	//supported input formats
	protected Format[] supportedOutFormats = new Format[0];	//supported output formats
	
	protected Float volume = 2.0F;						//volume scale factor
	protected Float pitch = 1.0F;						
	protected Float speed = 1.0F;
	
	Buffer in = new Buffer();
	Buffer out = new Buffer();
	PullDataSource effectSource;
	PullSourceStream effectStream;
	
	String outPath;
	
	/*public Footsteps(Float volume, Float pitch, Float speed) {
		this.volume = volume;
		this.pitch = pitch;
		this.speed = speed;
	}
	
	public Footsteps() {
		this.volume = volume;
		this.pitch = pitch;
		this.speed = speed;
	}*/
	
	//Formate initialisieren
	public Footsteps(Float volume, Float pitch, Float speed){
		supportedInFormats = new Format[] {
				new AudioFormat (
						AudioFormat.LINEAR,
						Format.NOT_SPECIFIED,
						16,
						Format.NOT_SPECIFIED,
						AudioFormat.LITTLE_ENDIAN,
						AudioFormat.SIGNED,
						16,
						Format.NOT_SPECIFIED,
						Format.byteArray
				)		
		};
	
		supportedOutFormats = new Format[] {
				new AudioFormat (
						AudioFormat.LINEAR,
						Format.NOT_SPECIFIED,
						16,
						Format.NOT_SPECIFIED,
						AudioFormat.LITTLE_ENDIAN,
						AudioFormat.SIGNED,
						16,
						Format.NOT_SPECIFIED,
						Format.byteArray
				)		
		};
		this.volume = volume;
		this.pitch = pitch;
		this.speed = speed;
	}
	
	public void open () throws ResourceUnavailableException {
		
		
		try {
			MediaLocator effectPath = new MediaLocator("file:C:\\DATEN\\Java workspace\\jfootsteps\\explo.wav");
			effectSource = (PullDataSource)Manager.createDataSource(effectPath);
			effectSource.connect();
			effectSource.start();
			effectStream = effectSource.getStreams()[0];
			
		} catch (NoDataSourceException e) {
			System.out.println("failure in open");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("failure in open");
			e.printStackTrace();
		}
		
		//System.out.append("fs_95");
	}
	
	public void close(){
	}
	
	public void reset(){
	}
	
	public Object[] getControls (){
		return (Object[]) new Control[0];
	}
	
	public Object getControl(String controlType) {
		try{
			Class cls = Class.forName(controlType);
			Object cs[] = getControls();
			for (int i = 0; i<cs.length; i++) {
				if (cls.isInstance(cs[i]))
					return cs[i];
			}
			return null;
		}catch (Exception e) {
			return null;
		}
	}
	
	//set Inputformat
	public Format setInputFormat(Format input) {
		inputFormat = (AudioFormat)input;
		return (Format)inputFormat;
	}
	
	//set Outputformat
	public Format setOutputFormat(Format output) {
		outputFormat = (AudioFormat)output;
		return (Format)outputFormat;
	}
	
	//get Inputformat
	protected Format getInputFormat() {
		return inputFormat;
	}
	
	//get OutputFormat
	protected Format getOutputFormat() {
		return outputFormat;
	}
	
	//get supported InputFormats
	public Format[] getSupportedInputFormats() {
		return supportedInFormats;
	}
	
	//	Outputformats f�r das gew�hlte inputformat
	public Format[] getSupportedOutputFormats(Format in) {
		if (!(in instanceof AudioFormat))
			return new Format[0];
		
		AudioFormat iaf = (AudioFormat)in;
		if(!iaf.matches(supportedInFormats[0]))
			return new Format[0];
		
		AudioFormat oaf = new AudioFormat(
				AudioFormat.LINEAR,
				iaf.getSampleRate(),
				16,
				iaf.getChannels(),
				AudioFormat.LITTLE_ENDIAN,
				AudioFormat.SIGNED,
				16,
				Format.NOT_SPECIFIED,
				Format.byteArray
			);
		
		return new Format[]{oaf};
	}
	

	public void setVolume(Float newVolume) {
		this.volume = newVolume;
	}
	
	public void setPitch(Float newPitch) {
		this.pitch = newPitch;
	}
	
	public void setSpeed(Float newSpeed) {
		this.speed = newSpeed;
	}
	
	//get Name
	public String getName() {
		return effectName;
	}
	
	public int process(Buffer inputBuffer, Buffer outputBuffer) {
		// prolog
		byte[] inData = (byte[])inputBuffer.getData();
		int inLength = inputBuffer.getLength();
		int inOffset = inputBuffer.getOffset();
		
		 byte[] outData = validateByteArraySize(outputBuffer, inLength);
		 int outOffset = outputBuffer.getOffset();
		 

		//System.out.append("fs_211");
		 int samplesNumber = inLength/2;
		 
		 //main
		 
		 //Open effect file
		 try {
			open();
		} catch (ResourceUnavailableException e) {
			System.out.println("open l�uft nicht");
			e.printStackTrace();
		}
		
		 byte[] effectData = new byte[inLength];
		 try {
			effectStream.read(effectData,0,inLength);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		 
		 for (int i = 0; i < samplesNumber; i++) {
			 int tempL = inData[inOffset++];
			 int tempH = inData[inOffset++];
			 int sample = tempH | (tempL & 255);
			 
			 /*int tempLe = effectData[inOffset++];
			 int tempHe = effectData[inOffset++];
			 int effectSample = tempHe | (tempLe & 255);*/
			 
			 sample = (int)(sample*volume);
			 //sample = (int)(sample+effectSample);
			 
			 if (sample>32767)
				 sample = 32767;
			 else if (sample<-32768)
				 sample = -32768;
			 
			 outData[outOffset++] = (byte) (sample & 255);
			 outData[outOffset++] = (byte) (sample >> 8);
		 }
		 
		 // epilog
		 updateOutput(outputBuffer, outputFormat, samplesNumber, 0);
		 return BUFFER_PROCESSED_OK;
	}
	
	protected byte[] validateByteArraySize(Buffer buffer, int newSize){
		Object objectArray = buffer.getData();
		byte[] typedArray;
		
		if(objectArray instanceof byte[]) {
			typedArray = (byte[])objectArray;
			if(typedArray.length >= newSize)
				return typedArray;
		}
		
		System.out.println(getClass().getName() + " : allocating byte["+newSize+"] ");
		typedArray = new byte[newSize];
		buffer.setData(typedArray);
		return typedArray;
	}
	
	protected void updateOutput(Buffer outputBuffer, Format format, int length, int offset) {
		outputBuffer.setFormat(format);
		outputBuffer.setLength(length);
		outputBuffer.setOffset(offset);
		
	}
	


}
