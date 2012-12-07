package com.isometric.toolkit.actoractions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Class to manage the various scripted actions through code and the python api
 * 
 * @author Erik
 * 
 */
public class ActionQueue
{
  Logger log = LoggerFactory.getLogger();
  private List<Actionable> actions = new ArrayList<Actionable>();

  private Actor actor;
  private Actionable currentAction;
  
  private boolean actionsNotAllowed = false;

  /***
   * ActionQueue's belong to actors, the ActionQueue must also observe the actor inorder to modify it. 
   * @param actor
   */
  public ActionQueue (Actor actor)
  {
    this.actor = actor;
  }

  /***
   * Returns the actor that owns this queue.
   * @return internal Actor
   */
  public Actor getActor(){
    return this.actor;
  }
  
  /***
   * Adds an action to the internal action queue
   * 
   * @param action
   * @throws RepeatForeverException 
   */
  public void add (Actionable action) throws RepeatForeverException
  {
    if(actionsNotAllowed)
      throw new RepeatForeverException("The action RepeatForever cannot have any actions after it");
      
    actions.add(action);
    
    
    if(action instanceof RepeatForever){
      actionsNotAllowed = true;
    }
  }

  /***
   * Removes an action from the internal action queue
   * 
   * @param action
   */
  public void remove (Actionable action)
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
  public Actionable getAction (int index)
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
      if (currentAction.isComplete(actor)) {
        log.info("Action Completed: " + currentAction.getClass());

        actions.remove(currentAction);
      }
    }
  }
}
