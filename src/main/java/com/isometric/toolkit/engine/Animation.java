package com.isometric.toolkit.engine;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_LINEAR;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.isometric.toolkit.ToolKitMain;


public class Animation implements Drawable
{
  static Logger logger = Logger.getLogger(Animation.class);

  // Assumes a linear horizontal spriteshteed
  private int height; // Height of each individual sprite in the spritesheet
  private int width; // Width of each individual sprite in the spritesheet
  private int offset; // Number of sprites to skip from the beginning
  private int length; // Number of total sprites in the sprite sheet
  private float speed; // Number of seconds until the next image
  
  // List of textures in in animation
  private List<Texture> sprites = new ArrayList<Texture>();
  private int currIndex;
  private int maxIndex;
  
  // Image tick
  long startTime = System.currentTimeMillis();
  
  // OpenGL Stuff
  private static IntBuffer textureIDBuffer;

  public Animation (String ref, int height, int width, int offset, int length,
                    float speed)
  {
    this.height = height;
    this.width = width;
    this.offset = offset;
    this.length = length;
    this.speed = speed;
    loadSprites(ref);

  }

  private static int createTextureID ()
  {
    glGenTextures(textureIDBuffer);
    return textureIDBuffer.get(0);
  }

  private void loadSprites (String ref)
  {
    BufferedImage image = null;
    try {
      image = ImageIO.read(Animation.class.getResourceAsStream(ref));
    }
    catch (Exception e) {
      logger.error("Failed to load spritesheet: " + ref + " Exception: "
                   + e);
    }
    

    int totalImages = (int) Math.floor(image.getWidth()/this.width);
    
    BufferedImage tmp = null;
    for(int i = 0; i < totalImages; i++){
      if(i==this.length){
        break;
      }
      
      tmp = image.getSubimage((offset+i)*this.width, 0, this.width,this.height);
      
      
      
      int srcPixelFormat;
  
      int textureID = createTextureID();
      Texture texture = new Texture(GL_TEXTURE_2D, textureID);
  
      // bind this texture
      glBindTexture(GL_TEXTURE_2D, textureID);
  
      texture.setWidth(Math.min(tmp.getWidth(), width));
      texture.setHeight(Math.min(tmp.getHeight(), height));
  
      if (tmp.getColorModel().hasAlpha()) {
        srcPixelFormat = GL_RGBA;
      }
      else {
        srcPixelFormat = GL_RGB;
      }
  
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  
      /*
      WritableRaster raster;
      BufferedImage texImage;
      
      if (image.getColorModel().hasAlpha()) {
        raster =
          Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texture.getWidth(),
                                         texture.getHeight(), 4, null);
        texImage =
          new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
      }
      else {
        raster =
          Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texture.getWidth(),
                                         texture.getHeight(), 3, null);
        texImage =
          new BufferedImage(glColorModel, raster, false, new Hashtable());
      }*/
  
      ByteBuffer textureBuffer = null;
  
      byte[] data =
        ((DataBufferByte) tmp.getRaster().getDataBuffer()).getData();
  
      textureBuffer = ByteBuffer.allocateDirect(data.length);
      textureBuffer.order(ByteOrder.nativeOrder());
      textureBuffer.put(data, 0, data.length);
      textureBuffer.flip();
  
      // produce a texture from the byte buffer
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, get2Fold(tmp.getWidth()),
                   get2Fold(tmp.getHeight()), 0, srcPixelFormat,
                   GL_UNSIGNED_BYTE, textureBuffer);
    
    }

  }

  private static int get2Fold (int fold)
  {
    int ret = 2;
    while (ret < fold) {
      ret *= 2;
    }
    return ret;
  }

  public void tick ()
  {
    if((startTime - System.currentTimeMillis())/1000.f>speed){
      currIndex = (currIndex + 1) % maxIndex;
    }else{
      startTime = System.currentTimeMillis();
    }
    
  }

  public void draw (int x, int y)
  {
    tick();
    
    // store the current model matrix
    glPushMatrix();

    
    // bind to the appropriate texture for this sprite
    Texture texture = sprites.get(currIndex);
    int width = texture.getImageWidth();
    int height = texture.getImageHeight();
    texture.bind();

    // translate to the right location and prepare to draw
    glTranslatef(x, y, 0);

    // draw a quad textured to match the sprite
    glBegin(GL_QUADS);
    {
      glTexCoord2f(0, 0);
      glVertex2f(0, 0);

      glTexCoord2f(0, texture.getHeight());
      glVertex2f(0, height);

      glTexCoord2f(texture.getWidth(), texture.getHeight());
      glVertex2f(width, height);

      glTexCoord2f(texture.getWidth(), 0);
      glVertex2f(width, 0);
    }
    glEnd();

    // restore the model view matrix to prevent contamination
    glPopMatrix();
  }

}
