package com.isometric.toolkit.cameraactions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.cameras.*;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Class to manage the various scripted actions through code and the python api
 * 
 * @author Jeff
 * 
 */
public class CameraActionQueue
{
  Logger log = LoggerFactory.getLogger();
  private List<CameraActionable> actions = new ArrayList<CameraActionable>();

  private Camera camera;
  private CameraActionable currentAction;
  
  private boolean actionsNotAllowed = false;

  /***
   * ActionQueue's belong to cameras, the ActionQueue must also observe the camera in order to modify it. 
   * @param camera
   */
  public CameraActionQueue (Camera camera)
  {
    this.camera = camera;
  }

  /***
   * Returns the camera that owns this queue.
   * @return internal Camera
   */
  public Camera getCamera(){
    return this.camera;
  }
  
  /***
   * Adds an action to the internal action queue
   * 
   * @param action
   * @throws RepeatForeverException 
   */
  public void add (CameraActionable action) throws RepeatForeverException
  {
    if(actionsNotAllowed)
      throw new RepeatForeverException("The action RepeatForever cannot have any actions after it");
      
    actions.add(action);
    
    
    if(action instanceof CameraRepeatForever){
      actionsNotAllowed = true;
    }
  }

  /***
   * Removes an action from the internal action queue
   * 
   * @param action
   */
  public void remove (CameraActionable action)
  {
    actions.remove(action);
  }

  /***
   * Returns the size of the internal queue.
   * @return int Size
   */
  public int getSize ()
  {
    return actions.size();
  }

  /***
   * Returns the action at a particular index in the internal representation.
   * @param index
   * @return Action
   */
  public CameraActionable getAction (int index)
  {
    return actions.get(index);
  }

  /***
   * Returns true if there is an action after the current one.
   * @return boolean
   */
  public boolean hasNext ()
  {
    return actions.size() > 1;
  }

  /***
   * Update first thing in the action queue
   */
  public void update (float delta)
  {
    if (actions.size() > 0) {
      currentAction = actions.get(0);
      currentAction.update(delta);

      // log.info("Current Action Updating: " + currentAction.getClass());
      if (currentAction.isComplete(camera)) {
        log.info("Action Completed: " + currentAction.getClass());

        actions.remove(currentAction);
      }
    }
  }
}
