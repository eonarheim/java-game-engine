package com.isometric.toolkit.actoractions;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.InvalidArgumentException;

/***
 * Action to move an actor to a destination along a quadratic bezier curve with
 * a certain speed and bezier parameters.
 * See http://en.wikipedia.org/wiki/B%C3%A9zier_curve for information about
 * bezier curves.
 * 
 * @author Erik
 * 
 */
public class BezierBy implements Actionable
{
  private static Logger log = LoggerFactory.getLogger();

  Point[] points = null;
  Actor actor;
  float time = 0.f;
  float totalTime = 1.f;


  boolean hasStarted = false;

  /***
   * 
   * @param a actor to apply the action to
   * @param points array of three points to create of quadratic bezier curve
   * @param the total time spent in travel
   * @throws InvalidArgumentException
   */
  public BezierBy (Actor a, Point[] points, float totalTime)
    throws InvalidArgumentException
  {
    this.points = points;
    this.actor = a;

    this.totalTime = totalTime;
    this.time = totalTime;

    // Quadratic bezier requires three points
    if (points.length != 3) {
      throw new InvalidArgumentException(
                                         "Quadratic Bezier Curves require three points.");
    }
  }


  private Point bezier (float time)
  {
    float tmp = 1.f -time;
    log.debug(tmp);
    return points[0].scale((float) Math.pow(tmp, 2.f))
            .add(points[1].scale((2 * tmp)*time))
            .add(points[2].scale((float) Math.pow(time, 2.f)));
  }

  @Override
  public Point getPos ()
  {
    return actor.getPos();
  }

  @Override
  public void update (float delta)
  {
    if (!hasStarted()) {
      setStarted(true);
    }

    time -= delta;

    // Scale time on [0,1]
    Point p = this.bezier((totalTime-time)/totalTime);
    

    actor.setX(p.getX());
    actor.setY(p.getY());

    if (isComplete(actor)) {
      actor.setX(points[2].getX());
      actor.setY(points[2].getY());
      actor.setDx(0);
      actor.setDy(0);
    }

  }

  @Override
  public boolean isComplete (Actor a)
  {
    return time<0.f;
  }

  @Override
  public boolean hasStarted ()
  {
    return this.hasStarted;
  }

  private void setStarted (boolean hasStarted)
  {
    this.hasStarted = hasStarted;
  }

  @Override
  public void reset (Actor a)
  {
    this.time = this.totalTime;
    this.hasStarted = false;

  }

}
