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
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.KeyCombo;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;

public abstract class Actor
{
  private static Logger logger = LoggerFactory.getLogger();
  protected String type = "Actor";

  private float scale = 1.f;

  protected float x;
  protected float y;

  protected float dx;
  protected float dy;

  protected float width;
  protected float height;

  protected HashMap<String, Animation> animations =
    new HashMap<String, Animation>();
  protected HashMap<Integer, String> keyHooks = new HashMap<Integer, String>();
  protected HashMap<KeyCombo, Motion> motionHooks =
    new HashMap<KeyCombo, Motion>();
  
  // Motion stuff
  protected List<Motion> motionQueueDeltas = new ArrayList<Motion>();
  protected List<Point> motionQueueDestination = new ArrayList<Point>();
  protected List<String> motionQueueAnimations = new ArrayList<String>();
  
  private float oldDistance = 99999f;
  private long oldTime = System.currentTimeMillis();
  private boolean start = true;

  protected String currentAnimation = "";

  private String spriteSheetName = null;

  protected SpriteSheet spriteSheet = null;

  public World world = null;

  private boolean isometric = false;

 
  
  // TODO Eventually get rid of... we want to force an Actor to have a width and
  // height
  protected Actor (World w, float x, float y, float dx, float dy)
  {
    this.world = w;
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    
   
  }

  protected Actor (World w, float x, float y, float dx, float dy, float width,
                   float height)
  {
    this.world = w;
    this.x = x;
    this.y = y;
    this.dx = dx;
    this.dy = dy;
    this.width = width;
    this.height = height;
        
  }

  public static String getType ()
  {
    return Actor.class.toString();
  }

  protected void update (){
    if(start){
      oldTime = System.currentTimeMillis();
      start = false;
    }
    if(motionQueueDeltas.size() > 0){
      Motion m = motionQueueDeltas.get(0);
      Point dest = motionQueueDestination.get(0);
      logger.info("Distance to target: " + dest.distance(new Point(this.getX(), this.getY())));
      float tmpDistance = dest.distance(new Point(this.getX(),this.getY()));
      if(tmpDistance > oldDistance){
        motionQueueDeltas.remove(0);
        motionQueueDestination.remove(0);
        motionQueueAnimations.remove(0);   
        oldDistance = 999999f;
        start=true;
      }else if(!start){
        oldDistance = tmpDistance;
        //float elapsed = (System.currentTimeMillis() - oldTime)/1000.f;
       
        this.dx = m.getDx()*(1.f/60f);//*elapsed;
        this.dy = m.getDy()*(1.f/60f);//*elapsed;
        if(motionQueueAnimations.size()>0){
          this.currentAnimation = motionQueueAnimations.get(0);
        }
        //oldTime = System.currentTimeMillis();
      }
    }
  }

  abstract public void draw ();

  abstract boolean collides (Actor a);

  protected void drawBoundingBox ()
  {
    
    
    
    glPushMatrix();
    glDisable(GL_TEXTURE_2D);
    //glEnable(GL_COLOR_MATERIAL);
    glTranslatef(x,y,0);
    
   
    
    glBegin(GL_LINES);
    glColor4f(1.f, 1.f, 0.0f,1f);
    glVertex2f(0f, 0f);
    glVertex2f(width, 0);

    glVertex2f(width, 0);
    glVertex2f(width, height);

    glVertex2f(width, height);
    glVertex2f(0, height);

    glVertex2f(0, height);
    glVertex2f(0, 0);
    
    glEnd();
    glColor4f(1.f,1.f,1.f,1.f);
    glEnable(GL_TEXTURE_2D);
    
    //glEnable(GL_BLEND);
    //f.drawString(0, 5, "("+x+","+y+")");
    //glDisable(GL_BLEND);
    glPopMatrix();

    //glTranslatef((float)x,(float)y,0);

  }
  
  
  
  public void move(float x, float y, float time, boolean interrupt){
    Motion m = new Motion((x-this.getX() )/time,(y -this.getY() )/time);
    
    if(!interrupt){
      start = true;
      motionQueueDeltas.add(m);
      motionQueueDestination.add(new Point(x,y));
      motionQueueAnimations.add(this.currentAnimation);
    }
    
    
  }
  
  public void move(float x, float y, float time, String animation, boolean interrupt){
    Motion m = new Motion((x-this.getX() )/time,(y -this.getY() )/time);
    
    if(!interrupt){
      start = true;
      motionQueueDeltas.add(m);
      motionQueueDestination.add(new Point(x,y));
      motionQueueAnimations.add(animation);
    }
    
    
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

    this.width = this.width * scale;
    this.height = this.height * scale;
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
    return this.width;
  }

  public void setWidth (float width)
  {
    this.width = width;
  }

  public float getHeight ()
  {
    return this.height;
  }

  public void setHeight (float height)
  {
    this.height = height;
  }
}
