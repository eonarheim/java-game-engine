package com.isometric.toolkit.parser;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.KeyCombo;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;
import com.isometric.toolkit.entities.NonPlayer;
import com.isometric.toolkit.entities.Player;
import com.isometric.toolkit.entities.Tile;
import com.isometric.toolkit.entities.Trigger;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
//import com.isometric.toolkit.entities.Playable;

public class WorldBuilder
{
  

  static Logger logger = Logger.getLogger(WorldBuilder.class);
  
  public static World newWorld(){
    World w = new World();
    
    w.setWorldName("Default World");
    
    
    Player player = new Player(w, 0, 0);
    player.setWorld(w);
    
    SpriteSheet ss = new SpriteSheet("TestPlayer.png", 10, 1);
    ss.createAnimation(1, 2, "walkDown", 0.2f); //todo, left off: pass in an Animation name, use that to reference it in the future.
    ss.createAnimation(3, 4, "walkLeft", 0.2f); //erik says an actor can reference a spritesheet(s) and just say "walkUp" is your animation for walking up
    ss.createAnimation(6, 7, "walkUp", 0.2f); //and it'll go get the animation of that name from the spritesheet
    ss.createAnimation(8, 9, "walkRight", 0.2f);
    
    player.addAnimation("walkDown", ss.getAnimation("walkDown"));
    player.addKeyHook(Keyboard.KEY_DOWN, "walkDown");
    player.addMotionHook(new KeyCombo(Keyboard.KEY_DOWN,null), new Motion(0.f,1.f));
    
    player.addAnimation("walkLeft",  ss.getAnimation("walkLeft"));
    player.addKeyHook(Keyboard.KEY_RIGHT, "walkLeft");
    player.addMotionHook(new KeyCombo(Keyboard.KEY_RIGHT,null), new Motion(1.f,0.f));
    
    player.addAnimation("walkUp", ss.getAnimation("walkUp"));
    player.addKeyHook(Keyboard.KEY_UP, "walkUp");
    player.addMotionHook(new KeyCombo(Keyboard.KEY_UP,null), new Motion(0.f,-1.f));
    
    player.addAnimation("walkRight",  ss.getAnimation("walkRight"));
    player.addKeyHook(Keyboard.KEY_LEFT, "walkRight");
    player.addMotionHook(new KeyCombo(Keyboard.KEY_LEFT,null), new Motion(-1.f,0.f));
    
    player.setCurrentAnimation("walkDown");
    
    
    
    Level newLevel = new Level();
    newLevel.addActor(player);
    w.addLevel(newLevel);
    w.setCurrentLevel(newLevel);
    
    return w;
  }
  
  
  public static World parseWorld(String uri){
    World worldResult = new World();
    
    
    logger.info("Reading starting world file");
    
     
    XStream x = new XStream(new DomDriver());
    
    // World Alias's
    x.alias("worldName", String.class);
    x.alias("actors", Actor.class);
    x.alias("levels", Level.class);
    x.alias("currentLevel", Level.class);
    
    x.registerConverter(new ActorConverter());
    x.registerConverter(new LevelConverter());
    x.registerConverter(new AnimationConverter());
    x.registerConverter(new ImageConverter());
    
    
    
    worldResult = (World)x.fromXML(uri);
    
    /*Properties WorldProps = new Properties();
     * 
     * 
    
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
      
     
      Level newLevel = new Level();
      newLevel.addActor(player);
      w.addLevel(newLevel);
      w.setCurrentLevel(newLevel);
    }
    catch (Exception e){
      logger.error("Error creating object of type: " + playableProps.getProperty("Class"));
      e.printStackTrace();
    }*/
    
    
    
    
    return worldResult;
  }

  
  
  public static Level parseLevel(String xml){
    
    return new Level();
  }
  
  public static Player parsePlayer(String xml){
    
    return new Player(null, 0, 0);
  }
  
  public static Tile parseTile(String xml){
    return new Tile(null, 0, 0);
    
  }
  
  public static NonPlayer parseNonPlayer(String xml){
    return new NonPlayer(null, 0f, 0f, 0f, 0f);
  }
  
  public static Trigger parseTrigger(String xml){
    return new Trigger();
  }
  

}
