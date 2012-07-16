package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.World;


/***
 * Like an NPCs or Enemies
 * @author Erik
 *
 */
public class NonPlayer extends Actor
{
  
  static Logger logger = LoggerFactory.getLogger();
  

  public static String getType(){
    return NonPlayer.class.toString();
  }
  
  public NonPlayer (World w, float x, float y, float dx, float dy)
  {
    super(w, x, y, dx, dy);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void update ()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void draw ()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String toXml ()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  void fromXml (String xml)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  boolean collides (Actor a)
  {
    // TODO Auto-generated method stub
    return false;
  }

 
}
