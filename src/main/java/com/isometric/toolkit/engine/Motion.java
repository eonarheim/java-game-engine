package com.isometric.toolkit.engine;


public class Motion implements Comparable<Motion>
{

  private float dx = 0.f;
  private float dy = 0.f;

  public Motion (float dx, float dy)
  {
    this.setDx(dx);
    this.setDy(dy);
  }

  @Override
  public int compareTo (Motion o)
  {
    if (o.getDx() == this.getDx() && o.getDy() == this.getDy()) {
      // TODO Auto-generated method stub
      return 0;
    }else{
      return -1;
    }
  }

  public float getDx ()
  {
    return dx;
  }

  public void setDx (float dx)
  {
    this.dx = dx;
  }

  public float getDy ()
  {
    return dy;
  }

  public void setDy (float dy)
  {
    this.dy = dy;
  }
  
  public void normalize(){
    Point tmp = new Point(dx,dy);
    float magnitude = tmp.distance(new Point(0f,0f));
    this.dx = this.dx/ magnitude;
    this.dy = this.dy/ magnitude;
  }
  
  public void scale(float scale){
    this.dx *= scale;
    this.dy *= scale;
  }
  
  public float maxComponent(){
    return Math.max(Math.abs(dx), Math.abs(dx));
  }
  

}