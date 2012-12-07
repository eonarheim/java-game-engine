package com.isometric.toolkit.actoractions;

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

  Repeat sequence;
  
  public Sequence(Actor a){
    this.sequence = new Repeat(a,1);
  }
  
  @Override
  public Point getPos ()
  {
    return this.sequence.getPos();
  }

  @Override
  public void update (float delta)
  {
    this.sequence.update(delta);    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    return this.sequence.isComplete(a);
  }

  @Override
  public boolean hasStarted ()
  {
    return this.sequence.hasStarted();
  }

  @Override
  public void reset (Actor a)
  {
    this.sequence.reset(a);
    
  }




}
