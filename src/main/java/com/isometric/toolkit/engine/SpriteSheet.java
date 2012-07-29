package com.isometric.toolkit.engine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;

public class SpriteSheet
{
  private static Logger logger = LoggerFactory.getLogger();
  
  private Image internalImage;
  private String internalImagePath = "";
  
  private int horizontalCount = 0;
  private int verticalCount = 0;
  
  private List<Image> images = new ArrayList<Image>();
  //private List<Animation> animations = new ArrayList<Animation>();
  private HashMap<String, Animation> animations = new HashMap<String, Animation>();

  public SpriteSheet (String ref, int horizontalCount, int verticalCount)
  {
    setInternalImagePath(ref);
    internalImage = Image.loadImage(ref);
    this.setHorizontalCount(horizontalCount);
    this.setVerticalCount(verticalCount);
    
    int w = internalImage.getWidth();
    int h = internalImage.getHeight();
    int verticalSpacing = (int) Math.floor(w / horizontalCount);
    int horizontalSpacing = (int) Math.floor(h / verticalCount);

    for (int j = 0; j < verticalCount; j++) {
      for (int i = 0; i < horizontalCount; i++) {
        images.add(Image.loadSubimage(ref, verticalSpacing, horizontalSpacing,
                                      i, j));
      }
    }
  }
  
  public SpriteSheet (String ref, int horizontalCount, int verticalCount, InputStream foo)
  {
    setInternalImagePath(ref);
    internalImage = Image.loadImage(ref, foo);
    this.setHorizontalCount(horizontalCount);
    this.setVerticalCount(verticalCount);
    
    int w = internalImage.getWidth();
    int h = internalImage.getHeight();
    int verticalSpacing = (int) Math.floor(w / horizontalCount);
    int horizontalSpacing = (int) Math.floor(h / verticalCount);

    for (int j = 0; j < verticalCount; j++) {
      for (int i = 0; i < horizontalCount; i++) {
        images.add(Image.loadSubimage(ref, verticalSpacing, horizontalSpacing,
                                      i, j));
      }
    }
  }

  public Animation createAnimation (int fromImage, int toImage, String name, float speed)
  {
    Animation newAnimation =
      new Animation(images.subList(fromImage, toImage+1), speed); //plus 1 because toImage is inclusive
    //animations.add(newAnimation);
    animations.put(name,  newAnimation);
    return newAnimation;
  }
  
  public Animation getAnimation(String name)
  {
    return animations.get(name);
  }

  public List<Image> getImages (int from, int to)
  {
    List<Image> resultImages = new ArrayList<Image>();
    for (int i = from; i <= to; i++) {
      resultImages.add(images.get(i));
    }
    return resultImages;

  }
  

  public String getInternalImagePath ()
  {
    return internalImagePath;
  }

  public void setInternalImagePath (String internalImagePath)
  {
    this.internalImagePath = internalImagePath;
  }

  public int getHorizontalCount ()
  {
    return horizontalCount;
  }

  public void setHorizontalCount (int horizontalCount)
  {
    this.horizontalCount = horizontalCount;
  }

  public int getVerticalCount ()
  {
    return verticalCount;
  }

  public void setVerticalCount (int verticalCount)
  {
    this.verticalCount = verticalCount;
  }

}
