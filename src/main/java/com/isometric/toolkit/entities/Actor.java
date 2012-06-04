package com.isometric.toolkit.entities;

public abstract class Actor implements Actable
{
  protected String type = "Actor";

  public String getType ()
  {
    return type;
  }

  abstract protected void update ();
  abstract protected void draw ();
  
}
