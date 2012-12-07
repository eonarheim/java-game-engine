package com.isometric.toolkit.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.newdawn.slick.util.Log;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.actions.Actionable;
import com.isometric.toolkit.actions.ActionQueue;
import com.isometric.toolkit.engine.Trigger;


/***
 * Level or game screen abstraction for the engine, meant to be contained inside of a world.
 * 
 * @author Erik
 * @author Jeff
 *
 */
public class Level
{

  static Logger logger = LoggerFactory.getLogger();

  private String name = "Default Name";

  private List<Tile> backgroundLayer = new ArrayList<Tile>();
  private List<Actor> objectLayer = new ArrayList<Actor>();
  private List<Actor> foregroundLayer = new ArrayList<Actor>();
  private List<Trigger> triggers = new ArrayList<Trigger>();
  
  

  public Level ()
  {

  }

  
  
  public void update (float delta)
  {
    
    
    for (Tile t: backgroundLayer){
      t.update(delta);
    }
   
    for (Actor a: objectLayer) {
      a.update(delta);
    }

    for (Actor a: foregroundLayer) {
      a.update(delta);
    }

    for (Trigger t: triggers) {
      t.check();
    }

  }

  public void draw ()
  {
    for (Tile t: backgroundLayer){
      t.draw();
    }
    
    for (Actor a: objectLayer) {
      a.draw();
    }

    for (Actor a: foregroundLayer) {
      a.draw();
    }
    
    
  }

  public void addActor (Actor a)
  {
    objectLayer.add(a);
  }

  public void removeActor (Actor a)
  {
    objectLayer.remove(a);
  }
  
  public void addTile(Tile t){
    backgroundLayer.add(t);
    
  }
  
  public void removeTile(Tile t){
    backgroundLayer.remove(t);    
  }

  public String getName ()
  {
    return name;
  }
  @Override
  public String toString(){
    return name;    
  }

  public void setName (String name)
  {
    this.name = name;
  }

  public List<Actor> getObjectLayer ()
  {
    return this.objectLayer;
  }
}
