package com.isometric.toolkit.cameraactions;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.cameras.*;;
//import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Base action from which all other derive.
 * 
 * @author Jeff
 *
 */
public interface CameraActionable
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
  public boolean isComplete(Camera a);


  /***
   * Indicates whether an action has started execution
   * @return boolean
   */
  public boolean hasStarted();


  /***
   * Reset internal state to play the action again and re-perform any initialization calculations 
   */
  void reset (Camera a);
    
 

}
