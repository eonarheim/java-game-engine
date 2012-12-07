package com.isometric.toolkit.engine;

import static org.junit.Assert.*;

import org.junit.Test;

public class MotionTest
{

  @Test
  public void testConstructor()
  {
    Vector m = new Vector(.5f,.5f);
    assertNotNull(m);
  }
  
  @Test
  public void testGetSet(){
    Vector m = new Vector(.5f,.5f);
    assertEquals(m.getX(),.5f,.001);
    assertEquals(m.getY(),.5f,.001);
    m.setX(0f);
    assertEquals(m.getX(),0f,.001);
    assertEquals(m.getY(),.5f,.001);
    m.setY(0f);
    assertEquals(m.getY(),0f,.001);
    assertEquals(m.getX(),0f,.001);
    
  }
  
  @Test
  public void testComparison(){
    Vector m = new Vector(.5f,.5f);
    Vector m2 = new Vector(.5f,.5f);
    
    assertEquals(m.compareTo(m2),0);
    
    m2.setX(.6f);
    
    assertEquals(m.compareTo(m2),-1);
    
    
  }

}
