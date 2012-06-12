package com.isometric.toolkit.entities;

import java.util.ArrayList;
import java.util.List;

public class Level
{
  
  private List<Trigger> triggers = new ArrayList<Trigger>();
  
  public void update(){
    for(Trigger t : triggers){
      t.check();
    }
  }
  
  public void draw(){
    
  }
  

}
