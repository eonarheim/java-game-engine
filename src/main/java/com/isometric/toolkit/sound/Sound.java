package com.isometric.toolkit.sound;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

import com.isometric.toolkit.LoggerFactory;
import com.thoughtworks.xstream.annotations.XStreamAlias;



public class Sound
{
  
  private static Logger logger = LoggerFactory.getLogger();  
  
  
  private String soundPath = "";
  private boolean isLoaded = false;
  private boolean played = false;
  WaveData waveFile = null;
  
  /** Buffers hold sound data. */
  IntBuffer buffer = BufferUtils.createIntBuffer(1);
  
  /** Sources are points emitting sound. */
  IntBuffer source = BufferUtils.createIntBuffer(1);
  
  /** Position of the source sound. */
  FloatBuffer sourcePos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

  /** Velocity of the source sound. */
  FloatBuffer sourceVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

  /** Position of the listener. */
  FloatBuffer listenerPos = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

  /** Velocity of the listener. */
  FloatBuffer listenerVel = (FloatBuffer)BufferUtils.createFloatBuffer(3).put(new float[] { 0.0f, 0.0f, 0.0f }).rewind();

  /** Orientation of the listener. (first 3 elements are "at", second 3 are "up") */
  FloatBuffer listenerOri = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(new float[] { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f }).rewind();

  
  public Sound(String soundPath){
    this.soundPath = soundPath;

    waveFile = WaveData.create("sounds/"+soundPath);
  }  
  
  private int loadALData() {
    if(isLoaded){
      return AL10.AL_TRUE;
    }
    
    // Load wav data into a buffer.
    AL10.alGenBuffers(buffer);

    if(AL10.alGetError() != AL10.AL_NO_ERROR)
      return AL10.AL_FALSE;

    //Loads the wave file from your file system
    /*java.io.FileInputStream fin = null;
    try {
      fin = new java.io.FileInputStream("FancyPants.wav");
    } catch (java.io.FileNotFoundException ex) {
      ex.printStackTrace();
      return AL10.AL_FALSE;
    }
    WaveData waveFile = WaveData.create(fin);
    try {
      fin.close();
    } catch (java.io.IOException ex) {
    }*/

    //Loads the wave file from this class's package in your classpath

    AL10.alBufferData(buffer.get(0), waveFile.format, waveFile.data, waveFile.samplerate);
    //waveFile.dispose();

    // Bind the buffer with the source.
    AL10.alGenSources(source);

    if (AL10.alGetError() != AL10.AL_NO_ERROR)
      return AL10.AL_FALSE;

    AL10.alSourcei(source.get(0), AL10.AL_BUFFER,   buffer.get(0) );
    AL10.alSourcef(source.get(0), AL10.AL_PITCH,    1.0f          );
    AL10.alSourcef(source.get(0), AL10.AL_GAIN,     1.0f          );
    AL10.alSource (source.get(0), AL10.AL_POSITION, sourcePos     );
    AL10.alSource (source.get(0), AL10.AL_VELOCITY, sourceVel     );

    // Do another error check and return.
    if (AL10.alGetError() == AL10.AL_NO_ERROR){
      logger.info("Loading sound once: " +soundPath);
      //System.out.println(soundPath);
      isLoaded = true;
      return AL10.AL_TRUE;
    }
      

    return AL10.AL_FALSE;
  }
  
  void killALData() {
    AL10.alDeleteSources(source);
    AL10.alDeleteBuffers(buffer);
  }
  
  public synchronized void play() {
    new Thread(new Runnable() { // the wrapper thread is unnecessary, unless it blocks on the Clip finishing, see comments
      
      public void run() {
        //System.out.println("Play sound");
        // OpenAL implementation
        try{
          if(!AL.isCreated()){
            AL.create();
          }
        } catch (Exception le) {
          le.printStackTrace();
          return;
        }
        AL10.alGetError();

        // Load the wav data.
        if(loadALData() == AL10.AL_FALSE) {
          System.out.println("Error loading data.");
          return;
        }

        played = true;
        AL10.alSourcePlay(source.get(0));
        
        
        
        
        // Java implementation *sigh*
        /*
        try {
          
          

          logger.info("Attempting to play sound..."+soundPath);
          
          AudioInputStream inputStream = AudioSystem.getAudioInputStream(Sound.class.getClassLoader().getResourceAsStream("sounds/"+soundPath));

          Clip clip = AudioSystem.getClip();
          clip.open(inputStream);
          clip.start(); 
          logger.info("Playing sound..."+soundPath);
        } catch (Exception e) { 
          logger.error("Error playing sound: " + e.getStackTrace());
          e.printStackTrace();
        }*/
      }
    }).start();
  }
  

  public String getSoundPath ()
  {
    return soundPath;
  }

  public void setSoundPath (String soundPath)
  {
    this.soundPath = soundPath;
  }

  public boolean isPlayed ()
  {
    return played;
  }

  public void setPlayed (boolean played)
  {
    this.played = played;
  }
  

}
