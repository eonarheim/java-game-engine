package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

public class MoveBy extends Action
{

  public MoveBy (Actor a, Point p, float seconds)
  {
    super(a);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void update (ActionQueue actionQueue, float delta)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean isComplete ()
  {
    // TODO Auto-generated method stub
    return false;
  }

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

}
