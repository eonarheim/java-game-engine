package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Action to provide a delay (in seconds) until the next action in the queue.
 * 
 * @author Erik
 *
 */
public class Delay implements IAction
{

  @Override
  public Point getPos ()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void update (float delta)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean hasStarted ()
  {
    // TODO Auto-generated method stub
    return false;
  }


}
