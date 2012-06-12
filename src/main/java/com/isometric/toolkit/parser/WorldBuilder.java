package com.isometric.toolkit.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;
import com.isometric.toolkit.entities.Playable;

public class WorldBuilder
{
  

  static Logger logger = Logger.getLogger(WorldBuilder.class);
  
  
  public static World parseWorld(String uri){
    logger.info("Reading starting world file");
    Properties WorldProps = new Properties();
    
    try {
      logger.info("Loading world file: " + uri);
      WorldProps.load(WorldBuilder.class.getClassLoader().getResourceAsStream(uri));
      
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      logger.error("Could not load world file: " + uri);
      e.printStackTrace();
    }
    
    World w = new World();
    w.setWorldName(WorldProps.getProperty("WorldName"));
    String playableActor = "actors/"+WorldProps.getProperty("Playable");
    
    Properties playableProps = new Properties();
    
    try {
      logger.info("Loading playable actor file: " + playableActor);
      playableProps.load(WorldBuilder.class.getClassLoader().getResourceAsStream(playableActor));
      
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      logger.error("Could not load actor file: " + playableActor);
      e.printStackTrace();
    }
    
    
    Actor player = null;
    
    try {
      logger.info("Creating object of type: " + playableProps.getProperty("Class"));
      Class c = Class.forName(playableProps.getProperty("Class"));
      Constructor cons = c.getDeclaredConstructor(new Class[]{String.class, int.class, int.class, float.class, float.class, float.class, float.class});
      
      playableProps.remove("Class");
               
      player = (Actor) cons.newInstance((String)playableProps.get("SpriteRef"),
                                           Integer.parseInt((String) playableProps.get("Height")),
                                           Integer.parseInt((String) playableProps.get("Width")),
                                           Float.parseFloat((String) playableProps.get("X")),
                                           Float.parseFloat((String) playableProps.get("Y")),
                                           Float.parseFloat((String) playableProps.get("Dx")),
                                           Float.parseFloat((String) playableProps.get("Dy"))); 
      
      w.addActor(player);
      w.addLevel(new Level());
    }
    catch (Exception e){
      logger.error("Error creating object of type: " + playableProps.getProperty("Class"));
      e.printStackTrace();
    }
    
    
    
    
    return w;
  }
  
  

}
