package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;

public class Tile extends Actor {
  
  // Eveything should be sparse and debug level to avoid overlogging
  static Logger logger = LoggerFactory.getLogger();
  
  private boolean solid = false;
  private SpriteSheet spriteSheet = null;
  

  public static String getType(){
    return Tile.class.toString();
  }
  
  public Tile (World w, boolean solid, float x, float y)
  {
    super(w, x, y, 0, 0);
    this.setSolid(solid);
    
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

public boolean isSolid() {
	return solid;
}

public void setSolid(boolean solid) {
	this.solid = solid;
}

}
