package com.isometric.toolkit.actoractions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;


/***
 * Action to run a python script file in the interpreter.
 * 
 * @author Erik
 *
 */
public class RunScript implements Actionable
{

  
  public RunScript(Actor a, String filePath){
    
  }
  
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
