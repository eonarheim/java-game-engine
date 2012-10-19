package com.isometric.toolkit.actions;

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
public class RepeatForever extends Action
{
  Logger log = LoggerFactory.getLogger();
  ActionQueue actions;

  public RepeatForever (Actor a)
  {
    actions = new ActionQueue(a);
  }

  public void addAction (Action a)
  {
    actions.add(a);
  }

  /***
   * Cannot get the end of a repeat forever action.
   */
  public Point getEnd () throws RepeatForeverException
  {
    throw new RepeatForeverException("The action RepeatForever cannot have any actions after it, cannot retreive the end!");
  }

  public void setStart (Point start)
  {

  }

  public void update (Actor a, float delta)
  {

    if (actions.getSize() > 0) {
      Action current = actions.getAction(0);
      current.update(a, delta);
      if (current.isComplete(a)) {
        log.info("Action Completed: " + current.getClass());
        // Move current to the back of the queue
        actions.remove(current);
        actions.add(current);

      }

    }
  }

  public boolean isComplete (Actor a)
  {
    return false;
  }

}
