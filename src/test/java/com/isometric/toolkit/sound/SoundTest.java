package com.isometric.toolkit.sound;

import static org.junit.Assert.*;

import org.junit.Test;

public class SoundTest
{
  
  
  @Test
  public void testConstructor(){
    Sound s = new Sound("sonicring.wav");
    assertNotNull(s);
    assertEquals(s.getSoundPath(),"sonicring.wav");
  }
  
}
