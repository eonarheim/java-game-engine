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
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
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
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.isometric.toolkit.ToolKitMain;

public class Animation implements Drawable
{
  static Logger logger = Logger.getLogger(Animation.class);

  // Assumes a linear horizontal spriteshteed
  private int height; // Height of each individual sprite in the spritesheet
  private int width; // Width of each individual sprite in the spritesheet
  private float speed; // Number of seconds until the next image

  // List of textures in in animation
  // private List<Texture> sprites = new ArrayList<Texture>();

  private List<Image> sprites = new ArrayList<Image>();

  private int currIndex;
  private int maxIndex;
  private int ticker = 0;
  
  private float scale = 1.f;
  private int rotation = 0;

  public Animation (String ref, int height, int width, float speed)
  {
    this.height = height;
    this.width = width;
    this.setSpeed(speed);

    loadSprites(ref);
    this.maxIndex = this.sprites.size();

  }

  public Animation (List<Image> images, float speed)
  {
    sprites = images;
    this.maxIndex = this.sprites.size();
    this.speed = speed;
  }

  private void loadSprites (String ref)
  {
    BufferedImage image = null;
    try {
      image =
        ImageIO.read(Animation.class.getClassLoader()
                .getResourceAsStream("images/" + ref));
    }
    catch (Exception e) {
      logger.error("Failed to load spritesheet: " + ref + " Exception: " + e);
    }

    int totalHorizontalRows =
      (int) Math.floor(image.getWidth() / this.width) - 1;
    int totalVerticalRows =
      (int) Math.floor(image.getHeight() / this.height) - 1;

    for (int j = 0; j < totalVerticalRows; j++) {
      for (int i = 0; i < totalHorizontalRows; i++) {
        Image newImage = Image.loadSubimage(ref, width, height, i, j);
        sprites.add(newImage);
      }
    }

  }

  public void tick ()
  {

    if (ticker++ / 60.f > getSpeed()) {
      currIndex = (currIndex + 1) % maxIndex;
      ticker = 0;
    }
  }

  public void draw (int x, int y)
  {
    tick();
    sprites.get(currIndex).draw(x, y);
  }

  public float getSpeed ()
  {
    return speed;
  }

  public void setSpeed (float speed)
  {
    this.speed = speed;
  }

  public float getScale ()
  {
    return scale;
  }

  public void setScale (float scale)
  {
    this.scale = scale;
    for (Image i: sprites) {
      i.setScale(scale);
    }
  }

  public float getRotation ()
  {
    return rotation;
  }

  public void setRotation (int rotation)
  {
    this.rotation = rotation;
    for (Image i: sprites) {
      i.setRotation(rotation);
    }
  }

}
