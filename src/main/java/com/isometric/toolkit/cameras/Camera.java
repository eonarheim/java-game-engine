package com.isometric.toolkit.cameras;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.entities.Actor;

public class Camera
{
  private Actor actorToFollow = null;
  private float distanceFromCenter = 0.0f;
  private Point pos = new Point(0.f,0.f);
  private Motion shift = new Motion(0.f,0.f);
  private Motion trans = new Motion(0.f,0.f);
  
  
  private float width = 800;
  private float height = 600;
  private float scale = 1.0f;
  
  private float transx = 0f;
  private float transy = 0f;
  
  private float shiftx = 0f;
  private float shifty = 0f;
  
  private float rotate = 0f;
  
  public Camera(Actor a, float distanceFromCenter, float width, float height){
    actorToFollow = a;
    this.distanceFromCenter = distanceFromCenter;
    this.width = width;
    this.height = height;
    
    this.shift.setDx(width/2.f + a.getWidth()/2.f);
    this.shift.setDy(height/2.f - a.getHeight()/2.f);
    
    
        
  }
  
  public Motion getShift(){
    return shift;
  }
  
  public void applyTransform(){
    glTranslatef(shift.getDx(),shift.getDy(),0);
    glScalef(scale,scale,0);
    glRotatef(rotate,0,0,1.0f);
    glTranslatef(-shift.getDx(),-shift.getDy(),0);
    
    glTranslatef(trans.getDx()-pos.getX()+shift.getDx(),trans.getDy()-pos.getY()+shift.getDy(),0);
    
  }
  
  public void update(){
    
    
    //System.out.println("Distance: " + pos.distance(actorToFollow.getPos()) + " Threshold: "+ distanceFromCenter + " Camera Pos: ("+pos.getX()+","+pos.getY()+")");
    float distance = 0.0f;
    if((distance = pos.manhattanDistance(actorToFollow.getPos())) > distanceFromCenter){
      Motion m = pos.sub(actorToFollow.getPos());
      m.normalize();
      m.scale(distance-distanceFromCenter);
      
      //m.scale(distance);
      pos.sub(m);
    }
  
    if(Keyboard.isKeyDown(Keyboard.KEY_ADD) || Keyboard.isKeyDown(Keyboard.KEY_EQUALS)){
      
      scale+=.1;
      Window.writeToDebug("Scale: " + scale);
    }
    
    if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT) || Keyboard.isKeyDown(Keyboard.KEY_MINUS)){
      scale-=.1;
      Window.writeToDebug("Scale: " + scale);
    }
    
    if(Keyboard.isKeyDown(Keyboard.KEY_R)){
      rotate+=1f;
      Window.writeToDebug("Rotate: " + rotate);
    }
    
    if(Keyboard.isKeyDown(Keyboard.KEY_L)){
      rotate-=1f;
      Window.writeToDebug("Rotate: " + rotate);
    }
    
    
  }
  

  
  public float getDistanceFromCenter ()
  {
    return distanceFromCenter;
  }

  public void setDistanceFromCenter (float distanceFromCenter)
  {
    this.distanceFromCenter = distanceFromCenter;
  }

  public Actor getActorToFollow ()
  {
    return actorToFollow;
  }

  public void setActorToFollow (Actor actorToFollow)
  {
    this.actorToFollow = actorToFollow;
  }

  public float getX ()
  {
    return pos.getX();
  }

  public void setX (float x)
  {
    this.pos.setX(x);
  }

  public float getY ()
  {
    return pos.getY();
  }

  public void setY (float y)
  {
    this.pos.setY(y);
  }
  
  public void setScale(float scale){
    this.scale = scale;
  }
  
  public float getScale(){
    return this.scale;
    
  }

}
