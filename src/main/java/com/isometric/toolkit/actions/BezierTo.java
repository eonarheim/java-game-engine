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
public class BezierTo implements IAction
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
    
    
    
  }

  @Override
  public boolean isComplete (Actor a)
  {
    // TODO Auto-generated method stub
    return false;
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
