package com.isometric.toolkit.entities;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.KeyCombo;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Window;
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

    // TODO Auto-generated constructor stub
  }

  @Override
  public void update ()
  {
    // uh, right now checking all other actors
    // for (Actor a: super.world.getActors())
    //TODO improve collision detection logic
    for (Actor a: super.world.getCurrentLevel().getObjectLayer())
      if (this != a) {
        if (this.collides(a))
        {
          Window.writeToDebug("Collides!");
          a.setCurrentAnimation("walkUp"); //visual hack just to show collision
        }
      }
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
  boolean collides (Actor a)
  {
    // note: x,y coordinates for images start in upper left corner
    // if (right1 < left2 || left1 > right2 || bot1 < top2 || top1 > bot2) then no way it collides
    if ((this.getX() + this.getWidth()) < a.getX()
        || this.getX() > (a.getX() + a.getWidth())
        || (this.getY() + this.getHeight()) < a.getY()
        || this.getY() > (a.getY() + a.getHeight()))
      return false;

    return true;
  }

}
