package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.KeyCombo;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.World;

public class Player extends Actor
{

  static Logger logger = LoggerFactory.getLogger();

  public static String getType ()
  {
    return Player.class.toString();
  }

  public Player (World w, float x, float y)
  {
    super(w, x, y, 100, 100);

    logger.info("Instantiated Player");
    logger.info("Player xml:\n" + this.toXml());

    // TODO Auto-generated constructor stub
  }

  @Override
  public void update ()
  {

    for (int key: this.keyHooks.keySet()) {
      if (Keyboard.isKeyDown(key)) {
        String animation = keyHooks.get(key);
        this.setCurrentAnimation(animation);

      }
    }

    for (KeyCombo key: this.motionHooks.keySet()) {

      if ((key.getKey1() == null || Keyboard.isKeyDown(key.getKey1()))
          && (key.getKey2() == null || Keyboard.isKeyDown(key.getKey2()))) {
        Motion m = motionHooks.get(key);
        this.x += m.getDx();
        this.y += m.getDy();
      }
    }

    // TODO Auto-generated method stub

  }

  @Override
  public void draw ()
  {
    Animation a = null;
    if ((a = animations.get(this.getCurrentAnimation())) != null) {
      a.draw((int) this.getX(), (int) this.getY());
    }
  }

  @Override
  public String toXml ()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  void fromXml (String xml)
  {
    // TODO Auto-generated method stub

  }

  @Override
  boolean collides (Actor a)
  {
    // TODO Auto-generated method stub
    return false;
  }

}
