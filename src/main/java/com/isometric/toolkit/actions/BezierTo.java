package com.isometric.toolkit.actions;

import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.InvalidArgumentException;

/***
 * Action to move an actor to a destination along a quadratic bezier curve with a certain speed and bezier parameters.
 * See http://en.wikipedia.org/wiki/B%C3%A9zier_curve for information about bezier curves.
 * @author Erik
 *
 */
public class BezierTo implements Actionable
{
  
  
  Point[] points = null;
  Actor actor;
  float speed = 0.f;
  float time = 0.f;
  float totalTime = 1.f;
  
  float distance = 0.f;
  
  boolean hasStarted = false;
  
  /***
   * 
   * @param a actor to apply the action to
   * @param points an array of three points to create of quadratic bezier curve
   * @param speed the velocity at which the actor travels
   * @throws InvalidArgumentException 
   */
  public BezierTo(Actor a, Point[] points, float speed) throws InvalidArgumentException{
    this.points = points;
    this.actor = a;
    this.speed = speed;
    this.totalTime = points[0].distance(points[2])/speed;
    this.distance = points[0].distance(points[2]);
    
    // Quadratic bezier requires three points
    if(points.length != 3){
      throw new InvalidArgumentException("Quadratic Bezier Curves require three points.");
    }
  }
  
  
  private Point bezier(float time){
    float tmp = 1.f -time;
    return points[0].scale((float) Math.pow(tmp,2.f)).add(points[1].scale(2*tmp)).add(points[2].scale((float) Math.pow(time, 2.f)));
  }

  @Override
  public Point getPos ()
  {
    return actor.getPos();
  }

  @Override
  public void update (float delta)
  {
    if(!hasStarted()){
      setStarted(true);
      
      this.distance = points[0].distance(points[points.length-1]);
      
    }
    time += delta;
    
    // Scale time on [0,1]
    Point p = this.bezier(time/totalTime);
    
    actor.setX(p.getX());
    actor.setY(p.getY());
    
    
    if(isComplete(actor)){
      actor.setX(points[2].getX());
      actor.setY(points[2].getY());
      actor.setDx(0);
      actor.setDy(0);
    } 
    
    
    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    return a.getPos().distance(points[0]) >= distance;
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
    // TODO Auto-generated method stub
    
  }



}
