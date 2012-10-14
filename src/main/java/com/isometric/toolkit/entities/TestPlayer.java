package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.parser.WorldBuilder;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class TestPlayer extends Actor
{

  static Logger logger = LoggerFactory.getLogger();

  private SpriteSheet ss = null;

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

  public TestPlayer (String spriteRef, int height, int width, float x, float y,
                     float dx, float dy)
  {
	  super(new World(), x, y, dx, dy);
    //super(null, x, y, dx, dy);
    this.spriteRef = spriteRef;
    this.height = height;
    this.width = width;
    logger.info("Instantiated Player");
  }
  

  private void lazyInit ()
  {
    if (!hasInit) {

      logger.info("Entered lazyInit");

      ss = new SpriteSheet(spriteRef, 10, 1);

      walkSouth = new Animation(ss.getImages(1, 2), 0.2f);// new
                                                          // Animation(spriteRef,
                                                          // height, width, 1,
                                                          // 2, .2f);
      walkSouth.setScale(2);
      idle = walkSouth;// new Animation(spriteRef, height, width, 0, 1, .2f);
      walkEast = new Animation(ss.getImages(3, 4), 0.2f);// new
                                                         // Animation(spriteRef,
                                                         // height, width, 3, 2,
                                                         // .2f);
      walkEast.setScale(2);
      walkNorth = new Animation(ss.getImages(6, 7), 0.2f);// new
                                                          // Animation(spriteRef,
                                                          // height, width, 6,
                                                          // 2, .2f);
      walkNorth.setScale(2);
      walkWest = new Animation(ss.getImages(8, 9), 0.2f);// new
                                                         // Animation(spriteRef,
                                                         // height, width, 8, 2,
                                                         // .2f);
      walkWest.setScale(2);
      hasInit = true;
    }
  }

  @Override
  public void update (float delta)
  {
    lazyInit();

    keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP);
    keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN);
    keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT);
    keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT);

    if (keyUp) {
      dy = -2.5f;
      dx = 0;
    }
    else if (keyDown) {
      dy = 2.5f;
      dx = 0;
    }
    else if (keyRight) {
      dx = 2.5f;
      dy = 0;
    }
    else if (keyLeft) {
      dx = -2.5f;
      dy = 0;
    }
    else {
      dy = dx = 0.0f;
    }

    x += dx;
    y += dy;

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
      logger.info("Walk North");
      walkSouth.draw((int)this.x, (int)this.y);
      return;
    }    
    else if(keyRight){
      logger.info("Walk Right");
      walkEast.draw((int)this.x, (int)this.y);
      return;

    } else if(keyLeft){
        logger.info("Walk West");
        walkWest.draw((int)this.x, (int)this.y);
        return;
           
      
    }else{
      idle.draw((int)this.x, (int)this.y);
    }
    
    
  }

boolean collides(Actor a) {
	// TODO Auto-generated method stub
	return false;
}
}
