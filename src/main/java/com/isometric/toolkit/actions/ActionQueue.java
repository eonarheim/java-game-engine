package com.isometric.toolkit.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Class to manage the various scripted actions through the python api
 * 
 * @author Erik
 * 
 */
public class ActionQueue
{
  Logger log = LoggerFactory.getLogger();
  private List<Action> actions = new ArrayList<Action>();

  private long oldTime = 0L;
  private Actor actor;

  public ActionQueue (Actor actor)
  {
    this.actor = actor;
  }

  public Actor getActor(){
    return this.actor;
  }
  
  /***
   * Adds an action to the internal action queue
   * 
   * @param action
   */
  public void add (Action action)
  {
    try {
      if (actions.size() > 0) {

        action.setStart(actions.get(actions.size() - 1).getEnd());
      }

      actions.add(action);
    }
    catch (RepeatForeverException e) {
      e.printStackTrace();
    }
  }

  /***
   * Removes an action from the internal action queue
   * 
   * @param action
   */
  public void remove (Action action)
  {
    actions.remove(action);
  }

  public int getSize ()
  {
    return actions.size();
  }

  public Action getAction (int index)
  {
    return actions.get(index);
  }

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
      Action currentAction = actions.get(0);
      currentAction.update(actor, delta);

      // log.info("Current Action Updating: " + currentAction.getClass());
      if (currentAction.isComplete(actor)) {
        log.info("Action Completed: " + currentAction.getClass());

        actions.remove(currentAction);
      }
    }
  }
}
