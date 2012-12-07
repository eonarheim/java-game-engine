package com.isometric.toolkit.engine;


/***
 * Basic point abstraction for the engine.
 * 
 * @author Erik
 *
 */
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
  
  //TODO: This is not manhattanDistance rename this.
  public float manhattanDistance(Point p){
    return Math.max(Math.abs(p.getX() - this.getX()), Math.abs(p.getY() - this.getY()));
    
  }
  
  
  public void add(Vector m){
    this.x += m.getX();
    this.y += m.getY();
  }
  
  public Point add(Point p){
    return new Point(this.x+p.x,this.y+p.y);
  }
  
  public void sub(Vector m){
    this.x -= m.getX();
    this.y -= m.getY();
    
  }
  
  public float dot(Point p){
    return this.x*p.x + this.y+p.y;
  }
  
  public Point scale(float val){
    return new Point(this.x*val, this.y*val);
  }
  
  public Vector sub(Point p){
    return new Vector(this.x - p.getX(),this.y - p.getY());
  }
  
  @Override
  public String toString(){
    return String.format("(%f,%f)", this.x,this.y);
  }
  
}
