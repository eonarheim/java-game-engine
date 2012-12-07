package com.isometric.toolkit.actoractions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Base action from which all other derive.
 * 
 * @author Erik
 *
 */
public interface Actionable
{
    
  
  /***
   * Returns the current position of the actor
   * @return Point
   */
  public Point getPos();
  
    
  /***
   * Updates the actor in a certain way for the scripted action
   */
  public void update(float delta);
  
  
  /***
   * Indicates whether an action is complete
   */
  public boolean isComplete(Actor a);


  /***
   * Indicates whether an action has started execution
   * @return boolean
   */
  public boolean hasStarted();


  /***
   * Reset internal state to play the action again and re-perform any initialization calculations 
   */
  void reset (Actor a);
    
 

}
