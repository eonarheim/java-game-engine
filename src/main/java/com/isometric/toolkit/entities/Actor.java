package com.isometric.toolkit.entities;

import com.isometric.toolkit.engine.Sprite;
import com.isometric.toolkit.engine.TextureLoader;

public abstract class Actor implements Actable
{
  protected String type = "Actor";
  
  protected float x;
  protected float y;
  
  protected float dx;
  protected float dy;
  
  protected Sprite sprite;
  
  private TextureLoader tl;
  
  
  protected Actor(TextureLoader tl, String ref, float x, float y, float dx, float dy){
    
    
    this.sprite = new Sprite(tl,ref);
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
  }
  

  public String getType ()
  {
    return type;
  }

  abstract protected void update ();
  protected void draw (){
    sprite.draw((int) x, (int) y);
  }

  public float getX ()
  {
    return x;
  }

  public void setX (float x)
  {
    this.x = x;
  }

  public float getY ()
  {
    return y;
  }

  public void setY (float y)
  {
    this.y = y;
  }

  public float getDx ()
  {
    return dx;
  }

  public void setDx (float dx)
  {
    this.dx = dx;
  }

  public float getDy ()
  {
    return dy;
  }

  public void setDy (float dy)
  {
    this.dy = dy;
  }
  
}
