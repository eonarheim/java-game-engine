package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Action to provide a delay (in seconds) until the next action in the queue.
 * 
 * @author Erik
 *
 */
public class Delay implements Actionable
{
  
  private Actor actor;
  private float delay;
  private float elapsedTime = 0.f;
  private boolean hasStarted = false;
  
  public Delay(Actor actor, float delay){
    this.actor = actor;
    this.delay = delay;
    
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
    }
    
    elapsedTime += delta;
    
    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    return elapsedTime >= delay;
  }
  
  @Override
  public void reset(Actor a){
    elapsedTime = 0.f;
  }

  @Override
  public boolean hasStarted ()
  {
    return hasStarted;
  }


}
