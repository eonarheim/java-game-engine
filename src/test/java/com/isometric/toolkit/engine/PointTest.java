package com.isometric.toolkit.engine;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTest
{

  @Test
  public void testConstructor ()
  {
    Point p = new Point(1,1);
    assertNotNull(p);
  }
  
  @Test
  public void testGetSet(){
    Point p = new Point(1f,255f);
    assertEquals(p.getX(),1f,.001);
    assertEquals(p.getY(),255f,.001);
    p.setX(255f);
    p.setY(1f);
    assertEquals(p.getY(),1f,.001);
    assertEquals(p.getX(),255f,.001);
    
  }
  
  @Test
  public void testDistance(){
    Point p = new Point(255f,0);
    Point p2 = new Point(0f,0f);
    
    assertEquals(p.distance(p2),255f,.001);
    
    p.setX(0f);
    p.setY(4f);
    
    p2.setX(3f);
    p2.setY(0f);
    
    assertEquals(p.distance(p2),5f,.001);
    
    
  }

}
