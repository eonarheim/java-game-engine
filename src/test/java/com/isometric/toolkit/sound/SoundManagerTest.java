package com.isometric.toolkit.sound;

import static org.junit.Assert.*;

import org.junit.Test;

public class SoundManagerTest
{

  @Test
  public void testConstructor ()
  {
    SoundManager sm = new SoundManager();
    assertNotNull(sm);
  }
  
  @Test
  public void testAdd(){
    SoundManager sm = new SoundManager();
    sm.addSound("a", new Sound("sonicring.wav"));
    sm.getSound("a").setSoundPath("1.wav");
    sm.addSound("b", new Sound("sonicring.wav"));
    assertEquals(sm.getSound("a").getSoundPath(),"1.wav");
    assertEquals(sm.getSound("b").getSoundPath(),"sonicring.wav");
    
  }
  
  @Test
  public void testRemove() {
    SoundManager sm = new SoundManager();
    sm.addSound("a", new Sound("sonicring.wav"));
    sm.addSound("b", new Sound("sonicring.wav"));
    
    sm.removeSound("a");
    
    assertEquals(sm.getSound("a"),null);
  
  }

}
