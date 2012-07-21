package com.isometric.toolkit.engine;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;


import com.isometric.toolkit.LoggerFactory;

public class Console
{
  private static Logger logger = LoggerFactory.getLogger();
  private static Console single = null;
  private static World world = null;
  
  Integer eventKey = null;
  char eventChar = '~';
  boolean drawn = false;

  private Font font = new Font("Consolas", Font.PLAIN, 15);
  private UnicodeFont f = new UnicodeFont(font);

  private PythonInterpreter interpreter = null;
  
  
  // Cursor related variables
  private float cursorX = 200;
  private float cursorY = 500;
  int currentLine = 0;
  boolean blink = false;
  long oldTime = System.currentTimeMillis();

  List<String> lines = new ArrayList<String>();
  List<String> output = new ArrayList<String>();
  //PyObject out = new PyObject();

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
    interpreter = new PythonInterpreter();
    interpreter.exec("from com.isometric.toolkit.engine import Console");
    single = this;
  }
  
  public static Console loadApi(){
    return single;
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
      if (eventChar == 8 && lines.get(currentLine).length() > 0) {
        String tmp = lines.get(currentLine);
        lines.set(currentLine, tmp.substring(0, tmp.length() - 1));
      }
      // Handle return
      if (eventChar == 13) {
        logger.info("Jython: " + lines.get(currentLine));
        try {
          
          ByteArrayOutputStream out = new  ByteArrayOutputStream();
          interpreter.setOut(out);
          interpreter.exec(lines.get(currentLine));
          if(out.toString().length()>0){
            for(String s : out.toString().split("\n")){
              lines.add(s);
              currentLine++;
            }
          }
        } catch (Exception e){
            //Window.writeToDebug(e.getMessage());
          lines.add("---Python syntax error---");
          currentLine++;
          logger.info(e.getMessage());       
        }
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
      
      f.drawString(getCursorX()+f.getWidth(lines.get(currentLine)), getCursorY(), "|");
    }
    
    float tmpY = getCursorY();
    for (int i = currentLine; i >= 0; i--) {
      f.drawString(getCursorX()-20, tmpY, "%>");
      f.drawString(getCursorX(), tmpY, lines.get(i));
      tmpY -= 15f;
    }
    GL11.glDisable(GL11.GL_BLEND);
  }

  public float getCursorX ()
  {
    return cursorX;
  }

  public void setCursorX (float cursorX)
  {
    this.cursorX = cursorX;
  }

  public float getCursorY ()
  {
    return cursorY;
  }

  public void setCursorY (float cursorY)
  {
    this.cursorY = cursorY;
  }

  public static World getWorld ()
  {
    return world;
  }

  public static void setWorld (World world)
  {
    Console.world = world;
  }
}
