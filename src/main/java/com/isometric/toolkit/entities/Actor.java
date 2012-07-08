package com.isometric.toolkit.entities;

import java.util.HashMap;

import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;



public abstract class Actor 
{
  protected String type = "Actor";
  
  protected float x;
  protected float y;
  
  protected float dx;
  protected float dy;
  
  protected HashMap<String, Animation> animations = new HashMap<String, Animation>();
  protected HashMap<Integer, String> keyHooks = new HashMap<Integer, String>();
  
  protected String currentAnimation = "";
  
  protected SpriteSheet spriteSheet = null;
  
  public World world = null;
  
  private boolean isometric = false;
  
  
  
  protected Actor(World w, float x, float y, float dx, float dy){
    this.world = w;
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
  }
  
  protected Actor(String xml){
    fromXml(xml);
  }
  

  public static String getType ()
  {
    return Actor.class.toString();
  }

  abstract public void update ();
  abstract public void draw ();
  abstract public String toXml();
  abstract void fromXml(String xml);
  abstract boolean collides(Actor a);

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

  public String getCurrentAnimation ()
  {
    return currentAnimation;
  }

  public void setCurrentAnimation (String currentAnimation)
  {
    this.currentAnimation = currentAnimation;
  }

  public World getWorld ()
  {
    return world;
  }

  public void setWorld (World worldRef)
  {
    this.world = worldRef;
  }
  
  public void addAnimation(String key, Animation value){
    this.animations.put(key, value);
  }
  
  public void removeAnimation(String key){
    this.animations.remove(key);
  }
  
  public void addKeyHook(Integer key, String name){
    this.keyHooks.put(key, name);
  }
  
  public void removeKey(Integer key){
    this.keyHooks.remove(key);
  }

  public SpriteSheet getSpriteSheet ()
  {
    return spriteSheet;
  }

  public void setSpriteSheet (SpriteSheet spriteSheet)
  {
    this.spriteSheet = spriteSheet;
  }

  public boolean isIsometric ()
  {
    return isometric;
  }

  public void setIsometric (boolean isometric)
  {
    this.isometric = isometric;
  }
    
  
}
