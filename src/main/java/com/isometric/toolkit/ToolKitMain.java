package com.isometric.toolkit;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.parser.WorldBuilder;

public class ToolKitMain
{

  static Logger logger = Logger.getLogger(ToolKitMain.class);

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
    
    
    logger.info("Starting Java RPG toolkit...");
    Window application = new Window();
    
    World gameWorld = null;
    try{
      gameWorld = WorldBuilder.parseWorld("test.world");//new World(ToolKitMain.class.getClassLoader().getResourceAsStream("start.world"));
    }catch (Exception e){
      logger.error("Failed to load starting world file exiting...");
      e.printStackTrace();
      System.exit(1);
    }
    
    application.init(gameWorld);
    application.start();
    logger.info("Stopping Java RPG toolkit...");
  }

}
