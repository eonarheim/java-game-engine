package com.isometric.toolkit.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Level
{
  static Logger logger = Logger.getLogger(Level.class);
  
  private Array<Float> backgroundLayer = new Array<Float()>; //This data type may need to change
  private List<Actor> objectLayer = new ArrayList<Actor>();
  private List<Actor> foregroundLayer = new ArrayList<Actor>(); //This data type may need to change
  
  private List<Trigger> triggers = new ArrayList<Trigger>();
    

public void update(){
	for(Actor a : objectLayer){
    a.update();
  }
	
	for(Actor a : foregroundLayer){
	    a.update();
	  }

    for(Trigger t : triggers){
      t.check();
    }
    
    
  }
  
  public void draw(){
	  for(Actor a : objectLayer){
	      a.draw();
	    }
	  
	  for(Actor a : foregroundLayer){
	      a.draw();
	    }	  
  }
  
  public void addActor(Actor a){
	  objectLayer.add(a);
	  }
	  
	  public void removeActor(Actor a){
		  objectLayer.remove(a);
	  }
  

}
