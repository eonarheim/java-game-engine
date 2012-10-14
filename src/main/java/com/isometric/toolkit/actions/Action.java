package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

public abstract class Action
{
  protected Actor actor;
  public Action(Actor a){
    actor = a;
  }
  
  /***
   * Returns the endpoint of the action
   * @return Point
   */
  public abstract Point getEnd();
  
  /***
   * Sets the start of the action
   */
  public abstract void setStart(Point start);
  
  
  /***
   * Updates the actor in a certain way for the scripted action
   */
  public abstract void update(ActionQueue a,float delta);
  
  /***
   * Indicates whether an action is complete
   */
  public abstract boolean isComplete();
  
  

}
