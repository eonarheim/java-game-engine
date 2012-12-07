package com.isometric.toolkit.engine;



/***
 * Basic motion vector for unit of time for the engine.
 * 
 * @author Erik
 *
 */
public class Vector implements Comparable<Vector>
{

  private float x = 0.f;
  private float y = 0.f;

  public Vector (float dx, float dy)
  {
    this.setX(dx);
    this.setY(dy);
  }

  @Override
  public int compareTo (Vector o)
  {
    if (o.getX() == this.getX() && o.getY() == this.getY()) {
      // TODO Auto-generated method stub
      return 0;
    }else{
      return -1;
    }
  }

  public float getX ()
  {
    return x;
  }

  public void setX (float dx)
  {
    this.x = dx;
  }

  public float getY ()
  {
    return y;
  }

  public void setY (float dy)
  {
    this.y = dy;
  }
  
  public void normalize(){
    Point tmp = new Point(x,y);
    float magnitude = tmp.distance(new Point(0f,0f));
    this.x = this.x/ magnitude;
    this.y = this.y/ magnitude;
  }
  
  public void scale(float scale){
    this.x *= scale;
    this.y *= scale;
  }
  
  public float maxComponent(){
    return Math.max(Math.abs(x), Math.abs(x));
  }
  

}