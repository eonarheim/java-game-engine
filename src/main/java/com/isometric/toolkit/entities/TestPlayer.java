package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.Sprite;
import com.isometric.toolkit.engine.TextureLoader;
import com.isometric.toolkit.parser.WorldBuilder;

public class TestPlayer extends Actor implements Playable
{
  

  static Logger logger = Logger.getLogger(TestPlayer.class);
  
  private Animation walkSouth = null;
  private Animation walkNorth = null;
  private Animation walkEast = null;
  private Animation walkWest = null;
  private Animation idle = null;
  
  private boolean keyUp = false;
  private boolean keyDown = false;
  private boolean keyLeft = false;
  private boolean keyRight = false;
  
  private String spriteRef = "";
  private int height;
  private int width;
  
  private boolean hasInit = false;

  public TestPlayer (String spriteRef, int height, int width, float x, float y, float dx, float dy)
  {
    super(x, y, dx, dy);
    this.spriteRef = spriteRef;
    this.height = height;
    this.width = width;
    logger.info("Instantiated Player");
  }
  private void lazyInit(){
    if(!hasInit){

      logger.info("Entered lazyInit");
      idle = new Animation(spriteRef, height, width, 0, 1, 1.5f);
      walkSouth = new Animation(spriteRef, height, width, 1, 2, 1.5f);
      walkNorth = new Animation(spriteRef, height, width, 3, 2, 1.5f);
      walkEast = new Animation(spriteRef, height, width, 6, 2, 1.5f);
      walkWest = new Animation(spriteRef, height, width, 8, 2, 1.5f);
      hasInit = true;
    }
  }
  
  

  @Override
  public void update ()
  {
    lazyInit();
    
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
      logger.info("Walk North");
      walkNorth.draw((int)this.x, (int)this.y);
      return;
    }   
    else if(keyDown){
      logger.info("Walk South");
      walkSouth.draw((int)this.x, (int)this.y);
      return;
    }    
    else if(keyLeft){
      logger.info("Walk West");
      walkWest.draw((int)this.x, (int)this.y);
      return;
    }    
    else if(keyRight){
      logger.info("Walk Right");
      walkEast.draw((int)this.x, (int)this.y);
      return;
    }else{
      idle.draw((int)this.x, (int)this.y);
    }
    
    
  }

}
