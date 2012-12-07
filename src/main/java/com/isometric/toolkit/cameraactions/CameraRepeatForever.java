package com.isometric.toolkit.cameraactions;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.cameras.*;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Action class that will repeat any action forever. No other action may come
 * after a repeat forever in an action queue.
 * 
 * @author Jeff
 * 
 */
public class CameraRepeatForever implements CameraActionable
{
  Logger log = LoggerFactory.getLogger();
  CameraActionQueue actions;
  Camera camera;
  
  private boolean hasStarted = false;
  

  public CameraRepeatForever (Camera a)
  {
    camera = a;
    actions = new CameraActionQueue(a);
  }

  public void addAction (CameraActionable a)
  {
    try {
      actions.add(a);
    }
    catch (RepeatForeverException e) {
      log.error(e.getMessage());
    }
  }


  public void update (float delta)
  {
    if(!hasStarted()){
      setStarted(true);
    }
    

    if (actions.getSize() > 0) {
    	CameraActionable current = actions.getAction(0);
      current.update(delta);
      if (current.isComplete(camera)) {
        log.info("Action Completed: " + current.getClass());
        
        // Move current to the back of the queue
        actions.remove(current);
        try {
          current.reset(camera);
          actions.add(current);
        }
        catch (RepeatForeverException e) {
          log.error(e.getMessage());
        }

      }

    }
  }

  @Override
  public boolean isComplete (Camera a)
  {
    return false;
  }

  @Override
  public Point getPos ()
  {
   return camera.getPos();
  }

  @Override
  public boolean hasStarted ()
  {
    return hasStarted;
  }
  
  private void setStarted(boolean hasStarted){
    this.hasStarted = hasStarted;
  }

  @Override
  public void reset (Camera a)
  {
    // TODO Auto-generated method stub
    
  }

}
