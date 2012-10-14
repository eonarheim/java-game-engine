package com.isometric.toolkit.entities;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.actions.Action;
import com.isometric.toolkit.actions.ActionQueue;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.KeyCombo;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;

public abstract class Actor
{
  private static Logger logger = LoggerFactory.getLogger();

  
  private ActionQueue actionQueue;
  
  private float scale = 1.f;

  protected float x;
  protected float y;

  protected float dx;
  protected float dy;

  protected HashMap<String, Animation> animations =
    new HashMap<String, Animation>();
  protected HashMap<Integer, String> keyHooks = new HashMap<Integer, String>();
  protected HashMap<KeyCombo, Motion> motionHooks =
    new HashMap<KeyCombo, Motion>();
  
  

  protected String currentAnimation = "";

  private String spriteSheetName = null;

  protected SpriteSheet spriteSheet = null;

  public World world = null;

  private boolean isometric = false;

  protected Actor (World w, float x, float y, float dx, float dy)
  {
    this.world = w;
    actionQueue = new ActionQueue(this);
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
  }

  
  public void addAction(Action a){
    actionQueue.add(a);
  }
  
  public void removeAction(Action a){
    actionQueue.remove(a);
  }
  public static String getType ()
  {
    return Actor.class.toString();
  }

  protected void update (float delta){
    actionQueue.update(delta);
    
  }
  
  public Point getPos(){
    return new Point(this.x,this.y);
  }
  
  public Motion getMotion(){
    return new Motion(this.dx,this.dy);
  }

  abstract public void draw ();

  abstract boolean collides (Actor a);

  protected void drawBoundingBox ()
  {
    glPushMatrix();
    
    // Disable textures before glLines
    glDisable(GL_TEXTURE_2D);
    glTranslatef(x,y,0);
    
    glBegin(GL_LINES);
    glColor4f(1.f, 1.f, 0.0f,1f);
    glVertex2f(0f, 0f);
    glVertex2f(this.getWidth(), 0);

    glVertex2f(this.getWidth(), 0);
    glVertex2f(this.getWidth(), this.getHeight());

    glVertex2f(this.getWidth(), this.getHeight());
    glVertex2f(0, this.getHeight());

    glVertex2f(0, this.getHeight());
    glVertex2f(0, 0);
    
    glEnd();
    glColor4f(1.f,1.f,1.f,1.f);
    glEnable(GL_TEXTURE_2D);
    
    glPopMatrix();


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

  public void addAnimation (String key, Animation value)
  {
    this.animations.put(key, value);
  }

  public void addAnimation (String key)
  {
    this.animations.put(key, this.world.getSpriteSheet(this.spriteSheetName)
            .getAnimation(key));
    this.setCurrentAnimation(key);
  }

  public void removeAnimation (String key)
  {
    this.animations.remove(key);
  }

  public void addKeyHook (Integer key, String name)
  {
    this.keyHooks.put(key, name);
  }

  public void removeKey (Integer key)
  {
    this.keyHooks.remove(key);
  }

  public void addMotionHook (KeyCombo key, Motion motion)
  {
    this.motionHooks.put(key, motion);
  }

  public void removeMotionHook (KeyCombo key)
  {
    this.motionHooks.remove(key);
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

  public float getScale ()
  {
    return scale;
  }

  public void setScale (float scale)
  {
    this.scale = scale;
    for (Animation a: this.animations.values()) {
      a.setScale(scale);
    }
  }

  public String getSpriteSheetName ()
  {
    return spriteSheetName;
  }

  public void setSpriteSheetName (String spriteSheetName)
  {
    this.spriteSheetName = spriteSheetName;
  }

  public float getWidth ()
  {
    return this.scale * this.world.getSpriteWidth();
  }

  public float getHeight ()
  {
    return this.scale * this.world.getSpriteHeight();
  }

  public void move (Motion delta)
  {
    this.x += delta.getDx();
    this.y += delta.getDy();
    this.dx = delta.getDx();
    this.dy = delta.getDy();
  }
}
