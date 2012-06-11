package com.isometric.toolkit.engine;

import java.io.InputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;


public class World
{
  static Logger logger = Logger.getLogger(World.class);
  
  private HashMap<String,Actor> actors = new HashMap<String, Actor>();
  
  private HashMap<String,Level> levels = new HashMap<String, Level>();
  
  private String currentLevel = "";
  
  private TextureLoader textureLoader = null;
  
  private String worldName = "";
  
  
  public World(InputStream in){
    
    
    logger.info("Reading starting world file");
    textureLoader = new TextureLoader();
    
    
    // TODO: Call parser to create hashmap data structures from world file
  }
  
  
  
  public void update(){
    
  }
  
  public void draw(){
    
    // TODO: Render code here
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    
    // Clear the screen and depth buffer
        
    // set the color of the quad (R,G,B,A)
    GL11.glColor3f(0.5f,0.5f,1.0f);
        
    // draw quad
    GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(100,100);
        GL11.glVertex2f(100+200,100);
        GL11.glVertex2f(100+200,100+200);
        GL11.glVertex2f(100,100+200);
    GL11.glEnd();
    
  }
  
  public void addActor(String key, Actor a){
    actors.put(key, a);
  }
  
  public void addLevel(String key, Level l){
    levels.put(key, l);
  }
  
  public void removeActor(String key){
    actors.remove(key);
  }
  
  public void removeLevel(String key){
    levels.remove(key);
  }
  
  

  public String getWorldName ()
  {
    return worldName;
  }

  public void setWorldName (String worldName)
  {
    this.worldName = worldName;
  }

  public String getCurrentLevel ()
  {
    return currentLevel;
  }

  public void setCurrentLevel (String currentLevel)
  {
    this.currentLevel = currentLevel;
  }

}
