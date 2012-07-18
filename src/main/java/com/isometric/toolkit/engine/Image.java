package com.isometric.toolkit.engine;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glScalef;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;

import com.isometric.toolkit.LoggerFactory;

public class Image implements Drawable
{

  static Logger logger = LoggerFactory.getLogger();
  private Texture texture;
  private float scale = 1.f;
  // in degrees
  private int rotation = 0;
  private int width = 0;
  private int height = 0;
  
  // things for serialization
  private String ref = "";
  private int horizontalOffset;
  private int verticalOffset;

  // OpenGL Stuff
  private static ComponentColorModel glAlphaColorModel =
    new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                            new int[] { 8, 8, 8, 8 }, true, false,
                            ComponentColorModel.TRANSLUCENT,
                            DataBuffer.TYPE_BYTE);

  private static ComponentColorModel glColorModel =
    new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                            new int[] { 8, 8, 8, 0 }, false, false,
                            ComponentColorModel.OPAQUE, DataBuffer.TYPE_BYTE);

  private static IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

  public Image ()
  {

  }

  public Image (Texture texture)
  {
    this.texture = texture;
  }

  public static Image loadSubimage (String ref, int width, int height,
                                    int horizontalOffset, int verticalOffset)
  {
    Image resultImage = new Image();
    resultImage.setRef(ref);
    resultImage.setHorizontalOffset(horizontalOffset);
    resultImage.setVerticalOffset(verticalOffset);

    BufferedImage image = null;
    try {
      image =
        ImageIO.read(Animation.class.getClassLoader()
                .getResourceAsStream("images/" + ref));
    }
    catch (Exception e) {
      logger.error("Failed to load spritesheet: " + ref + " Exception: " + e);
    }

    /*
     * int imageWidth = image.getWidth();
     * logger.info("Image width: " + imageWidth);
     * int imageStartWidthPoint = width * offset;
     * logger.info("Currently starting at width: " + imageStartWidthPoint);
     * int moduloWidth = (width * (offset + 1)) % imageWidth; //set offset to
     * one since we check the start image pt
     * logger.info("Modulo width: " + moduloWidth);
     */
    BufferedImage tmp = null;

    logger.info("Loading sprite. Vertical Offset(" + height + "): "
                + verticalOffset + " Horizontal Offset(" + width + "): "
                + horizontalOffset);
    
    if(image == null){
      logger.error("Internal image "+ref+" failed to load!");      
    }
    tmp =
      image.getSubimage((horizontalOffset) * width, verticalOffset * height,
                        width, height);

    int srcPixelFormat;

    int textureID = createTextureID();
    Texture texture = new Texture(GL_TEXTURE_2D, textureID);

    // bind this texture
    glBindTexture(GL_TEXTURE_2D, textureID);

    texture.setWidth(width);
    texture.setHeight(height);
    resultImage.setHeight(height);
    resultImage.setWidth(width);

    if (tmp.getColorModel().hasAlpha()) {
      srcPixelFormat = GL_RGBA;
    }
    else {
      srcPixelFormat = GL_RGB;
    }

    WritableRaster raster;
    BufferedImage texImage;

    int texWidth = 2;
    int texHeight = 2;

    // find the closest power of 2 for the width and height
    // of the produced texture
    while (texWidth < tmp.getWidth()) {
      texWidth *= 2;
    }
    while (texHeight < tmp.getHeight()) {
      texHeight *= 2;
    }

    texture.setTextureHeight(texHeight);
    texture.setTextureWidth(texWidth);

    if (tmp.getColorModel().hasAlpha()) {
      raster =
        Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth,
                                       texHeight, 4, null);
      texImage =
        new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
    }
    else {
      raster =
        Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth,
                                       texHeight, 3, null);
      texImage =
        new BufferedImage(glColorModel, raster, false, new Hashtable());
    }

    // copy the source image into the produced image
    Graphics g = texImage.getGraphics();
    g.setColor(new Color(0f, 0f, 0f, 0f));
    g.fillRect(0, 0, texWidth, texHeight);
    g.drawImage(tmp, 0, 0, null);

    ByteBuffer textureBuffer = null;

    byte[] data =
      ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

    textureBuffer = ByteBuffer.allocateDirect(data.length);
    textureBuffer.order(ByteOrder.nativeOrder());
    textureBuffer.put(data, 0, data.length);
    textureBuffer.flip();

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    // produce a texture from the byte buffer
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, get2Fold(tmp.getWidth()),
                 get2Fold(tmp.getHeight()), 0, srcPixelFormat,
                 GL_UNSIGNED_BYTE, textureBuffer);

    resultImage.setTexture(texture);

    return resultImage;
  }

  private static int get2Fold (int fold)
  {
    int ret = 2;
    while (ret < fold) {
      ret *= 2;
    }
    return ret;
  }

  public static Image loadImage (String ref)
  {
    Image resultImage = new Image();
    resultImage.setRef(ref);
    BufferedImage image = null;
    try {
      image =
        ImageIO.read(Animation.class.getClassLoader()
                .getResourceAsStream("images/" + ref));
    }
    catch (Exception e) {
      logger.error("Failed to load spritesheet: " + ref + " Exception: " + e);
    }

    BufferedImage tmp = null;

    logger.info("Sprite: " + ref + " is loading..");
    tmp = image;// image.getSubimage((offset) * width, 0, width, height);

    int srcPixelFormat;

    int textureID = createTextureID();
    Texture texture = new Texture(GL_TEXTURE_2D, textureID);

    // bind this texture
    glBindTexture(GL_TEXTURE_2D, textureID);

    texture.setWidth(tmp.getWidth());
    texture.setHeight(tmp.getHeight());

    resultImage.setWidth(tmp.getWidth());
    resultImage.setHeight(tmp.getHeight());

    if (tmp.getColorModel().hasAlpha()) {
      srcPixelFormat = GL_RGBA;
    }
    else {
      srcPixelFormat = GL_RGB;
    }

    WritableRaster raster;
    BufferedImage texImage;

    int texWidth = 2;
    int texHeight = 2;

    // find the closest power of 2 for the width and height
    // of the produced texture
    while (texWidth < tmp.getWidth()) {
      texWidth *= 2;
    }
    while (texHeight < tmp.getHeight()) {
      texHeight *= 2;
    }

    texture.setTextureHeight(texHeight);
    texture.setTextureWidth(texWidth);

    if (tmp.getColorModel().hasAlpha()) {
      raster =
        Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth,
                                       texHeight, 4, null);
      texImage =
        new BufferedImage(glAlphaColorModel, raster, false, new Hashtable());
    }
    else {
      raster =
        Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth,
                                       texHeight, 3, null);
      texImage =
        new BufferedImage(glColorModel, raster, false, new Hashtable());
    }

    // copy the source image into the produced image
    Graphics g = texImage.getGraphics();
    g.setColor(new Color(0f, 0f, 0f, 0f));
    g.fillRect(0, 0, texWidth, texHeight);
    g.drawImage(tmp, 0, 0, null);

    ByteBuffer textureBuffer = null;

    byte[] data =
      ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

    textureBuffer = ByteBuffer.allocateDirect(data.length);
    textureBuffer.order(ByteOrder.nativeOrder());
    textureBuffer.put(data, 0, data.length);
    textureBuffer.flip();

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    // produce a texture from the byte buffer
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, get2Fold(tmp.getWidth()),
                 get2Fold(tmp.getHeight()), 0, srcPixelFormat,
                 GL_UNSIGNED_BYTE, textureBuffer);

    resultImage.setTexture(texture);
    
    

    return resultImage;

  }

  private static int createTextureID ()
  {
    glGenTextures(textureIDBuffer);
    return textureIDBuffer.get(0);
  }

  @Override
  public void draw (int x, int y)
  {
    // TODO Auto-generated method stub

    // store the current model matrix
    glPushMatrix();

    // bind to the appropriate texture for this sprite

    float width = texture.getImageWidth() * scale;
    float height = texture.getImageHeight() * scale;
    texture.bind();

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    // translate to the right location and prepare to draw

    glRotatef(this.rotation, 0f, 0f, 1f);
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

    glDisable(GL_BLEND);
    // restore the model view matrix to prevent contamination
    glPopMatrix();

  }

  public Texture getTexture ()
  {
    return texture;
  }

  public void setTexture (Texture texture)
  {
    this.texture = texture;
  }

  public float getScale ()
  {
    return scale;
  }

  public void setScale (float scale)
  {
    this.scale = scale;
  }

  public int getRotation ()
  {
    return rotation;
  }

  public void setRotation (int degrees)
  {
    this.rotation = degrees;
  }

  public int getWidth ()
  {
    return width;
  }

  public void setWidth (int width)
  {
    this.width = width;
  }

  public int getHeight ()
  {
    return height;
  }

  public void setHeight (int height)
  {
    this.height = height;
  }

  public String getRef ()
  {
    return ref;
  }

  public void setRef (String ref)
  {
    this.ref = ref;
  }

  public int getHorizontalOffset ()
  {
    return horizontalOffset;
  }

  public void setHorizontalOffset (int horizontalOffset)
  {
    this.horizontalOffset = horizontalOffset;
  }

  public int getVerticalOffset ()
  {
    return verticalOffset;
  }

  public void setVerticalOffset (int verticalOffset)
  {
    this.verticalOffset = verticalOffset;
  }

}
