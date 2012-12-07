package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

public class ActorAnimation implements Actionable
{
  private Actor actor;
  private String animName;
  private boolean hasStarted = false;
  
  public ActorAnimation(Actor actor, String animName){
    this.actor = actor;
    this.animName = animName;
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
      actor.setCurrentAnimation(animName);
    }
  }

  @Override
  public boolean isComplete (Actor a)
  {
     return hasStarted;
  }

  @Override
  public boolean hasStarted ()
  {
    return hasStarted;
  }

  @Override
  public void reset (Actor a)
  {
    hasStarted = false;

  }

}
