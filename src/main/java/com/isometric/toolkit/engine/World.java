package com.isometric.toolkit.engine;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;


public class World
{
  static Logger logger = Logger.getLogger(World.class);
  
  private HashMap<String,Actor> actors = new HashMap<String, Actor>();
  
  private HashMap<String,Level> levels = new HashMap<String, Level>();
  
  private TextureLoader texture = null;
  
  
  public World(InputStream in){
    
    
    logger.info("Reading starting world file");
    texture = new TextureLoader();
    
    
    // TODO: Call parser to create hashmap data structures from world file
    
    
  }
  
  public void update(){
    
  }
  
  public void draw(){
    
  }

}
