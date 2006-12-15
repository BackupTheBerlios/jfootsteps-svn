
import javax.media.*;
import javax.media.format.*;


import com.sun.media.protocol.file.DataSource;


public class Footsteps implements Effect {
	
	private static String effectName = "Footsteps";		//Name
	protected AudioFormat inputFormat;					//inputformat
	protected AudioFormat outputFormat;					//outputformat
	protected Format[] supportedInFormats = new Format[0];	//supported input formats
	protected Format[] supportedOutFormats = new Format[0];	//supported output formats
	
	protected Float volume = 1.0F;						//volume scale factor
	protected Float pitch = 1.0F;						
	protected Float speed = 1.0F;
	
	Buffer in = new Buffer();
	Buffer out = new Buffer();
	
	String outPath;
	
	public Footsteps(Float volume, Float pitch, Float speed) {
		this.volume = volume;
		this.pitch = pitch;
		this.speed = speed;
	}
	
	public Footsteps() {
		this.volume = volume;
		this.pitch = pitch;
		this.speed = speed;
	}
	
	//Formate initialisieren
	public void footsteps(){
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
	}
	
	public void open () throws ResourceUnavailableException {
		
		MediaLocator effectPath = new MediaLocator("file" + "C:\\DATEN\\Java workspace\\tests\\Trumpet1.wav");
		DataSource effectSource = new DataSource();
		effectSource.setLocator(effectPath);
		in.setData(effectSource);
		
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
	
	//	Outputformats für das gewählte inputformat
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
		volume = newVolume;
	}
	
	public void setPitch(Float newPitch) {
		pitch = newPitch;
	}
	
	public void setSpeed(Float newSpeed) {
		speed = newSpeed;
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
		 
		 int samplesNumber = inLength/2;
		 
		 //main
		 for (int i = 0; i < samplesNumber; i++) {
			 int tempL = inData[inOffset++];
			 int tempH = inData[inOffset++];
			 int sample = tempH | (tempL & 255);
			 
			 sample = (int)(sample*volume);
			 
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
