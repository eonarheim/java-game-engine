package com.isometric.toolkit.entities;

import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.Sprite;
import com.isometric.toolkit.engine.TextureLoader;

public class TestPlayer extends Actor implements Playable
{
  
  private Animation walkSouth = null;
  private Animation walkNorth = null;
  private Animation walkEast = null;
  private Animation walkWest = null;
  private Animation idle = null;
  
  boolean keyUp = false;
  boolean keyDown = false;
  boolean keyLeft = false;
  boolean keyRight = false;

  public TestPlayer (String spriteRef, int height, int width, float x, float y, float dx, float dy)
  {
    super(x, y, dx, dy);
    
    idle = new Animation(spriteRef, height, width, 0, 1, .5f);
    walkSouth = new Animation(spriteRef, height, width, 1, 3, .5f);
    walkNorth = new Animation(spriteRef, height, width, 3, 5, .5f);
    walkEast = new Animation(spriteRef, height, width, 6, 8, .5f);
    walkWest = new Animation(spriteRef, height, width, 8, 10, .5f);
  }
  
  
  

  @Override
  public void update ()
  {
    keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP);
    keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
    keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
    keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
    
    if(keyUp){
      dy=.5f;
    }else if(keyDown){
      dy=-.5f;
    }else if(keyRight){
      dx=.5f;
    }else if(keyLeft){
      dx=-.5f;
    }else{
      dy = dx = 0.0f;
    }
    
    x+=dx;
    y+=dy;
    
  }

  @Override
  public void draw ()
  {
    
    
    if(keyUp){
      walkNorth.draw((int)this.x, (int)this.y);
      return;
    }   
    else if(keyDown){
      walkSouth.draw((int)this.x, (int)this.y);
      return;
    }    
    else if(keyLeft){
      walkWest.draw((int)this.x, (int)this.y);
      return;
    }    
    else if(keyRight){
      walkEast.draw((int)this.x, (int)this.y);
      return;
    }else{
      idle.draw((int)this.x, (int)this.y);
    }
    
    
  }

}
