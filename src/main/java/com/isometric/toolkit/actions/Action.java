package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Base action from which all other derive.
 * 
 * @author Erik
 *
 */
public abstract class Action
{
    
  /***
   * Returns the endpoint of the action
   * @return Point
   */
  public abstract Point getEnd() throws RepeatForeverException;
  
  /***
   * Sets the start of the action
   */
  public abstract void setStart(Point start);
  
  
  /***
   * Updates the actor in a certain way for the scripted action
   */
  public abstract void update(Actor a,float delta);
  
  /***
   * Indicates whether an action is complete
   */
  public abstract boolean isComplete(Actor a);
    

}
