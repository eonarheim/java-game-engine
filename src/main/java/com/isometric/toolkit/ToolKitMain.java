package com.isometric.toolkit;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.lwjgl.opengl.Display;

import com.isometric.toolkit.editor.GLEditor;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.parser.WorldBuilder;
import com.isometric.toolkit.sound.Sound;

public class ToolKitMain
{

  static Logger logger = LoggerFactory.getLogger();

  public static void main (String[] args)
  {
    try {
      PropertyConfigurator.configure(ToolKitMain.class.getClassLoader()
              .getResourceAsStream("log4j.properties"));
    }
    catch (Exception e) {
      System.out.println("Failed to initialize logger exiting...");
      e.printStackTrace();
      System.exit(1);
    }
    
    
    System.setProperty("org.lwjgl.input.Mouse.allowNegativeMouseCoords", "true");
    System.setProperty("sun.java2d.noddraw", "true");
    System.setProperty("sun.java2d.opengl", "false");
    
    final int desktopWidth = Display.getDesktopDisplayMode().getWidth();
    final int desktopHeight = Display.getDesktopDisplayMode().getHeight();
    // final Preferences prefs = Preferences.userNodeForPackage(c)

    
    final GLEditor editor = new GLEditor();
    editor.setFocusTraversalKeysEnabled(false);
    editor.setSize(desktopWidth, desktopHeight);

    editor.setVisible(true);
    editor.run(args);
    
    
   
    /*
    logger.info("Starting Java RPG toolkit...");
    Window application = new Window();
    
    World gameWorld = null;
    try{
      //gameWorld = WorldBuilder.newWorld();
      
      gameWorld = WorldBuilder.parseWorldFromFile("worlds/world.xml");
      //gameWorld = WorldBuilder.parseWorld("worlds/test.world");//new World(ToolKitMain.class.getClassLoader().getResourceAsStream("start.world"));
    }catch (Exception e){
      logger.error("Failed to load world file! Exiting program...");
      e.printStackTrace();
      System.exit(1);
    }
    
    application.init(gameWorld);
    application.start();
    logger.info("Stopping Java RPG toolkit...");
   */ 
  }

}
