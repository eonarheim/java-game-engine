package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

public class Blink implements Actionable
{
  private Actor actor;
  private float frequency;
  private float duration;
  
  private float elapsedTime = 0.f;
  private float nextBlink = 0.f;
  private int numBlinks = 0;
  private boolean isBlinking = false;
  
  private boolean hasStarted = false;
  
  
  public Blink(Actor a, float frequency, float duration){
    this.actor = a;
    this.frequency = frequency;
    this.duration = duration;
    this.numBlinks = (int) Math.floor(frequency * duration);
    
  }

  @Override
  public Point getPos ()
  {
    return actor.getPos();
  }

  @Override
  public void update (float delta)
  {
    if(!hasStarted()){
      hasStarted = true;
      this.nextBlink += duration/numBlinks;
      //actor.setDraw(false);
    }
    
    elapsedTime += delta;
    if(elapsedTime+.1f>nextBlink && nextBlink>elapsedTime-.1f){
      isBlinking = true;
      actor.setDraw(false);
    }else{
      if(isBlinking){
        isBlinking = false;
        nextBlink += duration/numBlinks;
      }
      
      actor.setDraw(true);      
    }
    
    if(isComplete(actor)){
      actor.setDraw(true);
    }
    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    return elapsedTime>=duration;
  }

  @Override
  public boolean hasStarted ()
  {
    return hasStarted;
  }

  @Override
  public void reset (Actor a)
  {
    elapsedTime = 0.f;
    nextBlink = 0.f;
    
    isBlinking = false;
  }


}
