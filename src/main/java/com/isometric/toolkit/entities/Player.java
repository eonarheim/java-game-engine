package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;

import com.isometric.toolkit.engine.World;

public class Player extends Actor
{
  
  static Logger logger = Logger.getLogger(Player.class);
  
  public static String getType(){
    return Player.class.toString();
  }
  

  public Player (World w, float x, float y)
  {
    super(w, x, y, 0, 0);

    logger.info("Instantiated Player");
    logger.info("Player xml:\n" + this.toXml());
    
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
