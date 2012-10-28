package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

/***
 * Action to move an Actor to a destination by a certain time in seconds.
 * 
 * @author Erik
 *
 */
public class MoveBy implements IAction
{
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
      this.calculatedSpeed = start.distance(destination)/maxTime;
    }
    
    
    elapsedTime += delta;
    
    Motion m = new Motion(this.move.getDx(),this.move.getDy());
    m.normalize();
    m.scale(calculatedSpeed*delta);
    actor.move(m);
    
    if(isComplete(actor)){
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
