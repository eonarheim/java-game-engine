package com.isometric.toolkit.sound;

import java.util.HashMap;
import java.util.Map;

public class SoundManager
{
  private static Map<String, Sound> sounds = new HashMap<String,Sound>();
  
  public static void addSound(String name, Sound sound){
    sounds.put(name, sound);
  }
  
  public static Sound getSound(String name){
    return sounds.get(name);
  }
  
  public static void playSound(String name){
    sounds.get(name).play();
  }
  
  public static void removeSound(String name){
    sounds.remove(name);
  }
  
}
