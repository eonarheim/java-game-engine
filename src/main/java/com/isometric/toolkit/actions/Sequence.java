package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

/***
 * Action to aggregate any number of actions in a row together as one action. Useful for complex behavior.
 * 
 * @author Erik
 *
 */
public class Sequence implements Actionable
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

  @Override
  public void reset (Actor a)
  {
    // TODO Auto-generated method stub
    
  }




}
