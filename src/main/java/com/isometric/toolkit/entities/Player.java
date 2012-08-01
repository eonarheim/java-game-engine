package com.isometric.toolkit.entities;

import java.awt.Font;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import static org.lwjgl.opengl.GL11.*;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.KeyCombo;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.sound.SoundManager;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@XStreamAlias("player")
public class Player extends CombatActor
{

  
  static Logger logger = LoggerFactory.getLogger();
  
  @XStreamOmitField
  protected Font font = null;//
  
  @XStreamOmitField
  protected UnicodeFont f = null;//

  public static String getType ()
  {
    return Player.class.toString();
  }

  public Player (World w, float x, float y)
  {
    super(w, x, y, 0, 0);

    

    logger.info("Instantiated Player");

    // TODO Auto-generated constructor stub
  }

  @Override
  public void update ()
  {
    super.update();
    // Set animation
    for (int key: this.keyHooks.keySet()) {
      if (Keyboard.isKeyDown(key)) {
        String animation = keyHooks.get(key);
        this.setCurrentAnimation(animation);
      }
    }

    // Set motion
    for (KeyCombo key: this.motionHooks.keySet()) {

      if ((key.getKey1() == null || Keyboard.isKeyDown(key.getKey1()))
          && (key.getKey2() == null || Keyboard.isKeyDown(key.getKey2()))) {
        Motion m = motionHooks.get(key);
        this.dx += m.getDx();
        this.dy += m.getDy();
      }
    }
    
    this.x += this.dx;
    this.y += this.dy;

    // TODO improve collision detection logic, right now doing an n^2 check
    for (Actor a: super.world.getActors())
      if (this != a) {
        if (this.collides(a)) {
          world.getSoundManager().playSound("ring");
          this.x -= this.dx;
          this.y -= this.dy;
        }
      }

    this.dx = 0;
    this.dy = 0;
  }

  @Override
  public void draw ()
  {
    if(font==null){
      font = new Font("Consolas", Font.BOLD, 10);      
    }
    
    if(f==null){
      f = new UnicodeFont(font);
      try {
        f.addAsciiGlyphs();
        f.getEffects().add(new ColorEffect(java.awt.Color.YELLOW));
        f.loadGlyphs();
      }
      catch (Exception e) {
        logger.error("Console Failed to load glyphs!: " + e.getMessage());
      }
    }
    

    Animation a = null;
    if ((a = animations.get(this.getCurrentAnimation())) != null) {
      a.draw((int) this.getX(), (int) this.getY());
    }
    if (Window.isDebug()) {
      this.drawBoundingBox();
      glPushMatrix();
      glTranslatef(x, y, 0);
      glEnable(GL_BLEND);
      f.drawString(0, -10, "(" + x + "," + y + ")");
      glDisable(GL_BLEND);
      glPopMatrix();
    }

  }

  @Override
  public boolean collides (Actor a)
  {
    // note: x,y coordinates for images start in upper left corner
    // if (right1 < left2 || left1 > right2 || bot1 < top2 || top1 > bot2) then
    // no way it collides
    if ((this.getX() + this.getWidth()) < a.getX()
        || this.getX() > (a.getX() + a.getWidth())
        || (this.getY() + this.getHeight()) < a.getY()
        || this.getY() > (a.getY() + a.getHeight()))
      return false;

    return true;
  }

}
