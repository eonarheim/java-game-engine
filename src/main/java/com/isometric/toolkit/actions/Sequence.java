package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

/***
 * Action to aggregate any number of actions in a row together as one action. Useful for complex behavior.
 * 
 * @author Erik
 *
 */
public class Sequence extends Action
{



  @Override
  public Point getEnd ()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setStart (Point start)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void update (Actor a, float delta)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    // TODO Auto-generated method stub
    return false;
  }

}
