package com.isometric.toolkit.engine;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.isometric.toolkit.LoggerFactory;

public class SpriteSheet
{
  private static Logger logger = LoggerFactory.getLogger();
  
  private Image internalImage;
  private String internalImagePath = "";
  private List<Image> images = new ArrayList<Image>();

  public SpriteSheet (String ref, int horizontalCount, int verticalCount)
  {
    setInternalImagePath(ref);
    internalImage = Image.loadImage(ref);
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

}
