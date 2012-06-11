package com.isometric.toolkit.entities;

import com.isometric.toolkit.engine.Sprite;
import com.isometric.toolkit.engine.TextureLoader;

public class TestPlayer extends Actor implements Playable
{

  protected TestPlayer (TextureLoader tl)
  {
    super(tl,"TestPlayer.png", 0.0f, 0.0f, 0.0f, 0.0f);
    
    
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void update ()
  {
    
  }

}
