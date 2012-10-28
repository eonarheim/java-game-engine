package com.isometric.toolkit.actions;

import org.apache.log4j.Logger;
import org.lwjgl.Sys;
import org.newdawn.slick.util.Log;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.entities.Actor;

/***
 * Action to move an Actor to a destination by a certain time in seconds.
 * 
 * @author Erik
 *
 */
public class MoveBy implements IAction
{

  Logger log = LoggerFactory.getLogger();
  float elapsedTime = 0.f;
  
  Actor actor;
  Point destination;
  float maxTime = 0.f;
  float calculatedSpeed = 0.f;

  private boolean hasStarted;
  
  Motion move;
  
  public MoveBy(Actor actor, Point destination, float time){
    this.actor = actor;
    this.destination = destination;
    this.maxTime = time;
    
    
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
      setStarted(true);
      Point start = getPos();
      move = destination.sub(start);
      move.normalize();
      log.info("Start: " + start);
      log.info("Distance to travel: " + start.distance(destination));
      log.info("Time (sec): " + this.maxTime);
      log.info("Timer resolution: " + Sys.getTimerResolution());
      log.info("Start time: " + Sys.getTime());
      
      this.calculatedSpeed = start.distance(destination)/maxTime;
      log.info("Calculated Speed: " + this.calculatedSpeed);
      log.info("Delta: " + delta);
      
      move.scale(calculatedSpeed);
    }
    
    
    elapsedTime += delta;
    
    Motion m = new Motion(this.move.getDx(),this.move.getDy());
    m.normalize();
    // this is off by a factor of 2, no idea why
    m.scale(calculatedSpeed*delta/2.f);
    System.out.println("Delta: " + delta);
    actor.move(m);
    
    if(isComplete(actor)){
      log.info("End point: " + actor.getPos());
      log.info("End speed: " + actor.getDx());
      log.info("End time: " + Sys.getTime());
      actor.setX(destination.getX()); 
      actor.setY(destination.getY());
      actor.setDx(0);
      actor.setDy(0);
    }
    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    return elapsedTime >= maxTime;
  }

  @Override
  public boolean hasStarted ()
  {
    return hasStarted;
  }
  
  private void setStarted(boolean hasStarted){
    this.hasStarted = hasStarted;
  }


}
