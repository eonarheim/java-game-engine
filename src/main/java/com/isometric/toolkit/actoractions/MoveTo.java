package com.isometric.toolkit.actoractions;


import com.isometric.toolkit.engine.Vector;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;



/***
 * Action to move an Actor to a destination at a certain speed.
 * 
 * @author Erik
 *
 */
public class MoveTo implements Actionable
{
  private Point start;
  private Point end;
  private Vector delta;
  
  private boolean hasStarted = false;
  
  float distance;
  float speed;  
  Actor actor;
  
/***
 * Action to move an actor to a destination at a certain speed.
 *  
 * @param actor
 * @param destination
 * @param speed
 */
  public MoveTo (Actor actor, Point destination, float speed)
  {
    // Store internal actor
    this.actor = actor;
    this.end = destination;
    this.speed = speed;
  }
    
  @Override
  public Point getPos()
  {
    return actor.getPos(); 
  }
  
  
  @Override
  public void update(float delta)
  {
    if(!hasStarted()){
      setStarted(true);
      this.start = actor.getPos();
      this.distance = start.distance(end);
      
      this.delta = end.sub(start);
      this.delta.normalize();      
    }
    
    
    Vector m = new Vector(this.delta.getX(),this.delta.getY());
    m.normalize();
    m.scale(speed*delta);
    actor.move(m);
    
    
    if(isComplete(actor)){
      actor.setX(end.getX());
      actor.setY(end.getY());
      actor.setDx(0);
      actor.setDy(0);
    } 
  }

  @Override
  public boolean isComplete (Actor a)
  {
    return a.getPos().distance(start) >= distance;
  }

  @Override
  public boolean hasStarted(){
    return this.hasStarted;
  }
  
  private void setStarted(boolean hasStarted){
    this.hasStarted = hasStarted;
  }

  @Override
  public void reset (Actor a)
  {
    hasStarted = false;
    
  }

}
