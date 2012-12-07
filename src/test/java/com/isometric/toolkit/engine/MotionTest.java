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
    assertEquals(m.getDx(),.5f,.001);
    assertEquals(m.getDy(),.5f,.001);
    m.setDx(0f);
    assertEquals(m.getDx(),0f,.001);
    assertEquals(m.getDy(),.5f,.001);
    m.setDy(0f);
    assertEquals(m.getDy(),0f,.001);
    assertEquals(m.getDx(),0f,.001);
    
  }
  
  @Test
  public void testComparison(){
    Vector m = new Vector(.5f,.5f);
    Vector m2 = new Vector(.5f,.5f);
    
    assertEquals(m.compareTo(m2),0);
    
    m2.setDx(.6f);
    
    assertEquals(m.compareTo(m2),-1);
    
    
  }

}
