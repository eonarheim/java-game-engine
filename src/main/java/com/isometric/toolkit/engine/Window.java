package com.isometric.toolkit.engine;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import com.isometric.toolkit.ToolKitMain;

public class Window
{

  static Logger logger = Logger.getLogger(Window.class);
  Vector<Integer> keyPress = new Vector<Integer>();

  
  private World gameWorld = null;
  private boolean calledInit = false;

  public Window ()
  {

    try {
      logger.info("Creating LWJGL display");
      
      Display.setDisplayMode(new DisplayMode(800, 600));
      Display.create();
      Display.setTitle("Java RPG Toolkit");
    }
    catch (LWJGLException e) {
      logger.error("Could not create LWJGL display! Exiting");
      e.printStackTrace();
      System.exit(1);
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
    GL11.glOrtho(0, 800, 0, 600, 1, -1);

    logger.info("Entering mainloop");
    // TODO: Implement FPS loading limiting
    while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

      gameWorld.update();
      gameWorld.draw();
      
      //checkInput();
      Display.update();
      Display.sync(60);
      
      
      
    }

    Display.destroy();
    logger.info("Close requested ... Display destroyed");

  }

  public void checkInput ()
  {
    if (Mouse.isButtonDown(0)) {
      int x = Mouse.getX();
      int y = Mouse.getY();

      System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
    }

    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
      System.out.println("SPACE KEY IS DOWN");
    }

    while (Keyboard.next()) {
      if (Keyboard.getEventKeyState()) {
        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
          System.out.println("A Key Pressed");
        }
        if (Keyboard.getEventKey() == Keyboard.KEY_S) {
          System.out.println("S Key Pressed");
        }
        if (Keyboard.getEventKey() == Keyboard.KEY_D) {
          System.out.println("D Key Pressed");
        }
      }
      else {
        if (Keyboard.getEventKey() == Keyboard.KEY_A) {
          System.out.println("A Key Released");
        }
        if (Keyboard.getEventKey() == Keyboard.KEY_S) {
          System.out.println("S Key Released");
        }
        if (Keyboard.getEventKey() == Keyboard.KEY_D) {
          System.out.println("D Key Released");
        }
      }
    }
  }

}
