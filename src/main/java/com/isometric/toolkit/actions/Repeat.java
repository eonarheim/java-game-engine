package com.isometric.toolkit.actions;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Action to repeat a certain action a certain number of times.
 * 
 * @author Erik
 *
 */
public class Repeat implements Actionable
{
  Logger log = LoggerFactory.getLogger();
  ActionQueue actions;
  Actor actor;
  
  private boolean hasStarted = false;
  private int counter = 0;
  private int numRepeats = 0;
  
  public Repeat (Actor a, int numRepeats)
  {
    actor = a;
    actions = new ActionQueue(a);
    this.numRepeats = numRepeats;
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
          counter++;
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
    return counter >= (numRepeats * actions.getSize());
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
    counter = 0;
    //hasStarted = false;
    
  }


}
