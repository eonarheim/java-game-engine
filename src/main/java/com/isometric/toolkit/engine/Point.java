package com.isometric.toolkit.engine;

public class Point
{
  private float x = 0.0f;
  private float y = 0.0f;
  public Point(float x, float y){
    this.x = x;
    this.y = y;
  }
  
  public float getX ()
  {
    return x;
  }
  public void setX (float x)
  {
    this.x = x;
  }
  public float getY ()
  {
    return y;
  }
  public void setY (float y)
  {
    this.y = y;
  }
  
  public float distance(Point p){
    return (float) Math.sqrt( Math.pow(Math.abs(p.getX() - this.getX()),2) + Math.pow(Math.abs(p.getY() - this.getY()),2)); 
  }
  
  public void add(Motion m){
    this.x += m.getDx();
    this.y += m.getDy();
  }
  
  public void sub(Motion m){
    this.x -= m.getDx();
    this.y -= m.getDy();
    
  }
  
  public Motion sub(Point p){
    return new Motion(this.x - p.getX(),this.y - p.getY());
  }
  
}
