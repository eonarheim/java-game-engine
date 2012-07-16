package com.isometric.toolkit.engine;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.ToolKitMain;

public class Window
{

  static Logger logger = LoggerFactory.getLogger();
  Vector<Integer> keyPress = new Vector<Integer>();
  
  private static List<String> debugList = new ArrayList<String>();
  private static List<Integer> timerList = new ArrayList<Integer>();

  private World gameWorld = null;
  private boolean calledInit = false;
  private static boolean debug = true;

  private Font font = new Font("HELVETICA", Font.PLAIN, 15);
  private UnicodeFont f = new UnicodeFont(font);

  private long lastFrame;

  private int fps;
  private int finalFps;
  private int ticker;

  private long lastFPS;

  public Window ()
  {

    try {
      logger.info("Creating LWJGL display");

      Display.setDisplayMode(new DisplayMode(800, 600));
      Display.create();
      Display.setTitle("Java RPG Toolkit");

      f.addAsciiGlyphs();
      f.getEffects().add(new ColorEffect(java.awt.Color.YELLOW));
      f.loadGlyphs();

    }
    catch (LWJGLException e) {
      logger.error("Could not create LWJGL display! Exiting");
      e.printStackTrace();
      System.exit(1);
    }
    catch (SlickException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void init (World w)
  {
    logger.info("Window initialized");
    this.calledInit = true;
    this.gameWorld = w;

  }

  public void start ()
  {
    if (!calledInit) {
      logger.error("Did not call init before start!");
      System.exit(1);
    }

    // init OpenGL
    logger.info("Initializing opengl..");
    GL11.glEnable(GL11.GL_TEXTURE_2D);
    GL11.glDisable(GL11.GL_DEPTH_TEST);
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(0, 800, 600, 0, 1, -1);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    GL11.glViewport(0, 0, 800, 600);
    GL11.glClearColor(0f, 1f, .2f, 1f);

    logger.info("Entering mainloop");
    
    getDelta(); // call once before loop to initialise lastFrame
    lastFPS = getTime(); // call before loop to initialise fps timer
    while (!Display.isCloseRequested()
           && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

      

      updateFPS();
      gameWorld.update();
      gameWorld.draw();
      // Debug text
      if (isDebug()) {
        float yoffset = 25.f;
        GL11.glEnable(GL11.GL_BLEND);
        f.drawString(10f, 10f, "FPS: " + finalFps);
        for(String s : debugList){
          f.drawString(10f, yoffset, s);
          yoffset += 15.f;
        }

        GL11.glDisable(GL11.GL_BLEND);
        
        
      }

       checkInput();
      Display.update();
      Display.sync(60);

    }

    Display.destroy();
    logger.info("Close requested ... Display destroyed");

  }

  public long getTime ()
  {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }
  
  public void tick ()
  {
    
    
    if(ticker++/60.f>1.f){
      for(int i=0; i<timerList.size();i++){
        Integer tmp = timerList.get(i);
        timerList.set(i, tmp - 1);
        if(tmp<=0){
          timerList.remove(i);
          debugList.remove(i);          
        }
      }
      ticker=0;
    }
  }

  /**
   * Calculate the FPS and set it in the title bar
   */
  public void updateFPS ()
  {
    tick();
    if (getTime() - lastFPS > 1000) {
      //Display.setTitle("FPS: " + fps);
      finalFps = fps;
      fps = 0;
      lastFPS += 1000;
    }
    fps++;
  }

  public int getDelta ()
  {
    long time = getTime();
    int delta = (int) (time - lastFrame);
    lastFrame = time;

    return delta;
  }

  
  public void checkInput ()
  {

    while (Keyboard.next()) {
      if (Keyboard.getEventKeyState()) {
        if (Keyboard.getEventKey() == Keyboard.KEY_D) {
          if(!isDebug()){
            System.out.println("Debug Enabled");
            logger.info("Debug enabled");
            setDebug(true);
          }else{
            System.out.println("Debug Disabled");
            logger.info("Debug Disabled");
            setDebug(false);
          }
        }
      }
      else {
        if (Keyboard.getEventKey() == Keyboard.KEY_D) {
        }
      }
    }
  }

  public static boolean isDebug ()
  {
    return debug;
  }
  
  private void setDebug(boolean b){
    debug = b;
  }
  
  
  public static void writeToDebug(String msg){
    debugList.add(msg);
    timerList.add(3);
  }

}
