package com.isometric.toolkit.actions;


import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

public class RepeatForever extends Action
{
  Logger log = LoggerFactory.getLogger();
  ActionQueue actions = new ActionQueue();

  public RepeatForever (Actor a)
  {
    super(a);
  }

  public void addAction (Action a)
  {
    actions.add(a);
  }

  public Point getEnd ()
  {

    return null;
  }

  public void setStart (Point start)
  {

  }

  public void update (ActionQueue a, float delta)
  {

    if (actions.getSize() > 0) {
      Action current = actions.getAction(0);
      current.update(actions, delta);
      if (current.isComplete()) {
        log.info("Action Completed: " + current.getClass());
        //Move current to the back of the queue
        actions.remove(current);
        actions.add(current);

      }

    }
  }

  public boolean isComplete ()
  {
    return false;
  }

}
