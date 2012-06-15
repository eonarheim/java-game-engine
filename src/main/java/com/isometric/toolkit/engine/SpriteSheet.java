package com.isometric.toolkit.engine;

import java.util.ArrayList;
import java.util.List;

public class SpriteSheet
{
  private Image internalImage;
  private List<Image> images = new ArrayList<Image>();
  
  
  public SpriteSheet(String ref, int horizontalCount, int verticalCount){
    internalImage = Image.loadImage(ref);
    int w = internalImage.getWidth();
    int spacing = (int) Math.floor(w/horizontalCount);
    
    for(int i = 0; i < horizontalCount; i++){
      images.add(Image.loadSubimage(ref, spacing, internalImage.getHeight(), i));
    }
    
  }
  
  public List<Image> getImages(int from, int to){
    List<Image> resultImages = new ArrayList<Image>();
    for(int i= from; i <= to; i++){
      resultImages.add(images.get(i));
    }
    return resultImages;
    
  }

}
