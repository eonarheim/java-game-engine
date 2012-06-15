package com.isometric.toolkit.entities;



public abstract class Actor implements Actable
{
  protected String type = "Actor";
  
  protected float x;
  protected float y;
  
  protected float dx;
  protected float dy;
  
  
  
  protected Actor(float x, float y, float dx, float dy){

    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
  }
  

  public String getType ()
  {
    return type;
  }

  abstract public void update ();
  abstract public void draw ();

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
