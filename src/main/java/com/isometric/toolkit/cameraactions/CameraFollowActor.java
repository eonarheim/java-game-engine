package com.isometric.toolkit.cameraactions;


import com.isometric.toolkit.engine.Vector;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.cameras.*;



/***
 * Action to move an Camera to a destination at a certain speed.
 * 
 * @author Jeff
 *
 */
public class CameraFollowActor implements CameraActionable
{
  private Point start;
  private Point end;
  private Vector delta;
  
  private boolean hasStarted = false;
  
  float distance;
  float speed;  
  Camera camera;
  
/***
 * Action to move an camera to a destination at a certain speed.
 *  
 * @param camera
 * @param destination
 * @param speed
 */
  public CameraFollowActor (Camera camera, Point destination, float speed)
  {
    // Store internal camera
    this.camera = camera;
    this.end = destination;
    this.speed = speed;
  }
    
  @Override
  public Point getPos()
  {
    return camera.getPos(); 
  }
  
  
  @Override
  public void update(float delta)
  {
    if(!hasStarted()){
      setStarted(true);
      this.start = camera.getPos();
      this.distance = start.distance(end);
      
      this.delta = end.sub(start);
      this.delta.normalize();      
    }
    
    
    Vector m = new Vector(this.delta.getX(),this.delta.getY());
    m.normalize();
    m.scale(speed*delta);
    camera.move(m);
    
    
    if(isComplete(camera)){
      camera.setX(end.getX());
      camera.setY(end.getY());
      camera.setDx(0);
      camera.setDy(0);
    } 
  }

  @Override
  public boolean isComplete (Camera a)
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
  public void reset (Camera a)
  {
    hasStarted = false;
    
  }

}
