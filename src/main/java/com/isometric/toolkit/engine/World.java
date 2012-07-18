package com.isometric.toolkit.engine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;
import com.isometric.toolkit.parser.WorldBuilder;


public class World
{
  static Logger logger = LoggerFactory.getLogger();
  
  private List<Actor> actors = new ArrayList<Actor>();
  
  private List<Level> levels = new ArrayList<Level>();
  
  private Level currentLevel = new Level(); 
    
  
  private String worldName = "";
  
  
  public World(){
    
    // TODO: Call parser to create hashmap data structures from world file
  }
  
  
  
  public void update(){
    if(Keyboard.isKeyDown(Keyboard.KEY_S)){
      WorldBuilder.writeWorld(this);
      Window.writeToDebug("Serializing world now!");
    }
    
    currentLevel.update();
    
  }
  
  public void draw(){
    

    // Clear the screen and depth buffer
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity(); 
        
 
    currentLevel.draw();
    
  }
  
  public void addActor(Actor a){
    actors.add(a);
  }
  
  public void addLevel(Level l){
    levels.add(l);
  }
  
  public void removeActor(Actor a){
    actors.remove(a);
  }
  
  public void removeLevel(Level l){
    levels.remove(l);
  }
  
  
  public String getWorldName ()
  {
    return worldName;
  }

  public void setWorldName (String worldName)
  {
    this.worldName = worldName;
  }

  public Level getCurrentLevel ()
  {
    return currentLevel;
  }

  public void setCurrentLevel (Level currentLevel)
  {
    this.currentLevel = currentLevel;
  }
  
  public List<Actor> getActors()
  {
    return this.actors;
  }

}
