package com.isometric.toolkit.engine;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.python.util.PythonInterpreter;

import com.isometric.toolkit.LoggerFactory;

public class Console
{
  private static Logger logger = LoggerFactory.getLogger();
  Integer eventKey = null;
  char eventChar = '~';
  boolean drawn = false;

  private Font font = new Font("Consolas", Font.PLAIN, 15);
  private UnicodeFont f = new UnicodeFont(font);

  // Cursor related variables
  float cursorX = 200;
  float cursorY = 50;
  int currentLine = 0;
  boolean blink = false;
  long oldTime = System.currentTimeMillis();

  List<String> lines = new ArrayList<String>();

  public Console ()
  {
    try {
      f.addAsciiGlyphs();
      f.getEffects().add(new ColorEffect(java.awt.Color.YELLOW));
      f.loadGlyphs();
    }
    catch (Exception e) {
      logger.error("Console Failed to load glyphs!: " + e.getMessage());
    }
    lines.add("");
  }
  
  private void tick ()
  {
    /* Old way, buggy when sharing animations
    if (ticker++ / 60.f > getSpeed()) {
      currIndex = (currIndex + 1) % maxIndex;
      ticker = 0;
    }
    */
    long newTime = System.currentTimeMillis();
    if((newTime - oldTime)/1000.f > .5f){
      blink = !blink;
      oldTime = newTime;
    }
    
  }

  public void update (char eventChar)
  {
    
    // eventKey = Keyboard.getEventKey();
    if (Window.isConsole()) {
      // Handle normal ascii
      if (eventChar >= 32 && eventChar <= 126) {
        this.eventChar = eventChar;
        lines.set(currentLine,
                  lines.get(currentLine) + String.valueOf(eventChar));
      }
      // Handle backspace
      if (eventChar == 8) {
        String tmp = lines.get(currentLine);
        lines.set(currentLine, tmp.substring(0, tmp.length() - 1));
      }
      // Handle return
      if (eventChar == 13) {
        logger.info("Jython: " + lines.get(currentLine));
        lines.add("");
        currentLine++;
      }

      if (Keyboard.getEventKeyState()) {
        drawn = false;
      }
    }
  }

  public void draw ()
  {
    tick();
    if (eventChar != '~' && !drawn) {
      drawn = true;
    }

    
    //glPushMatrix();
    // bind to the appropriate texture for this sprite

    
    //glEnable(GL_BLEND);
    //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    // translate to the right location and prepare to draw

    //glTranslatef(cursorX-50,cursorY-200, 0);
    //GL11.glColor4f(0.f,0.f,1.f,.5f);
    // draw a quad textured to match the sprite
    //glBegin(GL_QUADS);
    //{
     // glVertex2f(0, 0);

    //  glVertex2f(0, 200);

    //  glVertex2f(500, 200);

   //   glVertex2f(500, 0);
    //}
    //glEnd();
    //GL11.glColor4f(1.f,1.f,1.f,1.f);
    //glDisable(GL_BLEND);
    // restore the model view matrix to prevent contamination
    //glPopMatrix();
    
    GL11.glEnable(GL11.GL_BLEND);
    
    
    
    
    if(blink){
      
      f.drawString(cursorX+f.getWidth(lines.get(currentLine)), cursorY, "|");
    }
    
    float tmpY = cursorY;
    for (int i = currentLine; i >= 0; i--) {
      f.drawString(cursorX-20, tmpY, "%>");
      f.drawString(cursorX, tmpY, lines.get(i));
      tmpY -= 15f;
    }
    GL11.glDisable(GL11.GL_BLEND);
  }
}
