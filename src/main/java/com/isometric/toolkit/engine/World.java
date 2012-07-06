package com.isometric.toolkit.engine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;


public class World
{
  static Logger logger = Logger.getLogger(World.class);
  
  private List<Actor> actors = new ArrayList<Actor>();
  
  private List<Level> levels = new ArrayList<Level>();
  
  private Level currentLevel = new Level(); 
    
  
  private String worldName = "";
  
  
  public World(){
    
    // TODO: Call parser to create hashmap data structures from world file
  }
  
  
  
  public void update(){
   // for(Actor a : actors){
   //   a.update();
   // }
    
    currentLevel.update();
    
  }
  
  public void draw(){
    
    // TODO: Render code here
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
    GL11.glLoadIdentity();
    
    
    // Clear the screen and depth buffer
        
 
    currentLevel.draw();
    //for(Actor a : actors){
    //  a.draw();
    //}
    
    /*
    // set the color of the quad (R,G,B,A)
    GL11.glColor3f(0.5f,0.5f,1.0f);
        
    // draw quad
    GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(100,100);
        GL11.glVertex2f(100+200,100);
        GL11.glVertex2f(100+200,100+200);
        GL11.glVertex2f(100,100+200);
    GL11.glEnd();*/
    
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

}
