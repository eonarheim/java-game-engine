package com.isometric.toolkit.engine;

import java.io.InputStream;

import org.apache.log4j.Logger;


public class World
{
  static Logger logger = Logger.getLogger(World.class);
  public World(InputStream in){
    logger.info("Reading starting world file");
    
    // TODO: Call parser to create hashmap data structures from world file
    
  }
  
  public void update(){
    
  }
  
  public void draw(){
    
  }

}
