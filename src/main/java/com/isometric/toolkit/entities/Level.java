package com.isometric.toolkit.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.newdawn.slick.util.Log;

import com.isometric.toolkit.LoggerFactory;

public class Level
{
  
  
  static Logger logger = LoggerFactory.getLogger();
  
  private String name = "Default Name";

  private List<Float> backgroundLayer = new ArrayList<Float>(); // This data
                                                                // type may need
                                                                // to change
  private List<Actor> objectLayer = new ArrayList<Actor>();
  private List<Actor> foregroundLayer = new ArrayList<Actor>(); // This data
                                                                // type may need
                                                                // to change

  private List<Trigger> triggers = new ArrayList<Trigger>();
  
  public Level(){
    
  }

  public void update ()
  {
    if(objectLayer ==null){
      logger.error("Wuh? ObjectLayer is null!");
    }
    for (Actor a: objectLayer) {
      if(a==null){
        logger.error("Actor is null!");
      }
      a.update();
    }

    for (Actor a: foregroundLayer) {
      a.update();
    }

    for (Trigger t: triggers) {
      t.check();
    }

  }

  public void draw ()
  {
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

  public String getName ()
  {
    return name;
  }

  public void setName (String name)
  {
    this.name = name;
  }

}
