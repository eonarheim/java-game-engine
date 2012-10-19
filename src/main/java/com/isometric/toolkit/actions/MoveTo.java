package com.isometric.toolkit.actions;


import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;



/***
 * Action to move an Actor to a destination at a certain speed.
 * 
 * @author Erik
 *
 */
public class MoveTo extends Action
{
  Point start;
  Point end;
  Motion delta;
  float distance;
  float speed;

  public MoveTo (Actor actor, Point destination, float speed)
  {
    //System.out.println("Actor: " + a);
    end = destination;

    setStart(actor.getPos());    
    this.speed = speed;
  }
  
  public Point getEnd(){
    return end;
  }

  
  
  @Override
  public void update (Actor actor, float delta)
  {
    Motion m = new Motion(this.delta.getDx(),this.delta.getDy());
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
  public void setStart (Point start)
  {
    this.start = start;
    this.delta = end.sub(start);
    distance = start.distance(end);
    
  }

}
