package com.isometric.toolkit.sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager
{
  private Map<String, Sound> sounds = new HashMap<String,Sound>();
  
  public void addSound(String name, Sound sound){
    sounds.put(name, sound);
  }
  
  public Sound getSound(String name){
    return sounds.get(name);
  }
  
  public void playSound(String name){
    sounds.get(name).play();
  }
  
  public void removeSound(String name){
    sounds.remove(name);
  }
  
}
