package com.isometric.toolkit.entities;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class Level
{
  static Logger logger = Logger.getLogger(Level.class);
  
  
  private List<Trigger> triggers = new ArrayList<Trigger>();
  
  public void update(){
    for(Trigger t : triggers){
      t.check();
    }
  }
  
  public void draw(){
    
  }
  

}
