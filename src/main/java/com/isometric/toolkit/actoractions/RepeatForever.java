package com.isometric.toolkit.actoractions;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Action class that will repeat any action forever. No other action may come
 * after a repeat forever in an action queue.
 * 
 * @author Erik
 * 
 */
public class RepeatForever implements Actionable
{
  Logger log = LoggerFactory.getLogger();
  ActionQueue actions;
  Actor actor;
  
  private boolean hasStarted = false;
  

  public RepeatForever (Actor a)
  {
    actor = a;
    actions = new ActionQueue(a);
  }

  public void addAction (Actionable a)
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
      Actionable current = actions.getAction(0);
      current.update(delta);
      if (current.isComplete(actor)) {
        log.info("Action Completed: " + current.getClass());
        
        // Move current to the back of the queue
        actions.remove(current);
        try {
          current.reset(actor);
          actions.add(current);
        }
        catch (RepeatForeverException e) {
          log.error(e.getMessage());
        }

      }

    }
  }

  @Override
  public boolean isComplete (Actor a)
  {
    return false;
  }

  @Override
  public Point getPos ()
  {
   return actor.getPos();
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
  public void reset (Actor a)
  {
    // TODO Auto-generated method stub
    
  }

}
