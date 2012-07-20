package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.engine.World;

public class Tile extends Actor
{

  // Eveything should be sparse and debug level to avoid overlogging
  static Logger logger = LoggerFactory.getLogger();

  private boolean solid = false;

  public static String getType ()
  {
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
    Animation a = null;
    if ((a = animations.get(this.getCurrentAnimation())) != null) {
      a.draw((int) this.getX(), (int) this.getY());
    }
    
    if(Window.isDebug()){
      this.drawBoundingBox();
    }

  }

  @Override
  boolean collides (Actor a)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isSolid ()
  {
    return solid;
  }

  public void setSolid (boolean solid)
  {
    this.solid = solid;
  }

}
