package com.isometric.toolkit.actions;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;

public class MoveTo extends Action
{
  Point start;
  Point end;
  Motion delta;
  float distance;
  float speed;

  public MoveTo (Actor a, Point p, float speed)
  {
    super(a);
    System.out.println("Actor: " + a);
    end = p;

    setStart(a.getPos());    
    this.speed = speed;
    
    
    
  }
  
  public Point getEnd(){
    return end;
  }

  
  
  @Override
  public void update (ActionQueue actionQueue, float delta)
  {
    
    
    Motion m = new Motion(this.delta.getDx(),this.delta.getDy());
    m.normalize();
    m.scale(speed*delta);
    
    actor.move(m);
    if(isComplete()){
      actor.setX(end.getX());
      actor.setY(end.getY());
      actor.setDx(0);
      actor.setDy(0);
    }
    
  }

  @Override
  public boolean isComplete ()
  {
    return actor.getPos().distance(start) >= distance;
  }

  @Override
  public void setStart (Point start)
  {
    this.start = start;
    this.delta = end.sub(start);
    distance = start.distance(end);
    
  }

}
