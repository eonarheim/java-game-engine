package com.isometric.toolkit.cameras;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.entities.Actor;

public class Camera
{
  private Actor actorToFollow = null;
  private float distanceToEdge = 0.0f;
  private float x = 0.0f;
  private float y = 0.0f;
  private float width = 800;
  private float height = 600;
  private float scale = 1.0f;
  
  private float transx = 0f;
  private float transy = 0f;
  
  private float shiftx = 0f;
  private float shifty = 0f;
  
  private float rotate = 0f;
  
  public Camera(Actor a, float distanceToEdge, float width, float height){
    actorToFollow = a;
    this.distanceToEdge = distanceToEdge;
    this.width = width;
    this.height = height;
    
    this.shiftx = width/2.f + a.getWidth()/2.f;
    this.shifty = height/2.f - a.getHeight()/2.f;
    
    this.x = this.x + shiftx;//width/2.f + a.getWidth()/2.f;
    this.y = this.y + shifty;//height/2.f - a.getHeight()/2.f;
    
  }
  
  public void applyTransform(){
    glTranslatef(+shiftx,+shifty,0);
    glScalef(scale,scale,0);
    glRotatef(rotate,0,0,1.0f);
    glTranslatef(-shiftx,-shifty,0);
    
    glTranslatef(transx,transy,0);
    
  }
  
  public void update(){
    
    //if(distance() > distanceToEdge){
      transx = x - actorToFollow.getX() - actorToFollow.getWidth()/2.f;
      transy = y - actorToFollow.getY() - actorToFollow.getHeight()/2.f;
    //}
  
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
  
  private float distance(){
    return (float) Math.sqrt(Math.pow(x-actorToFollow.getX(), 2) + Math.pow(y-actorToFollow.getY(), 2));
  }
  
  public float getDistanceToEdge ()
  {
    return distanceToEdge;
  }

  public void setDistanceToEdge (float distanceToEdge)
  {
    this.distanceToEdge = distanceToEdge;
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

}
