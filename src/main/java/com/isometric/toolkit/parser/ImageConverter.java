package com.isometric.toolkit.parser;

import com.isometric.toolkit.engine.Image;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ImageConverter implements Converter
{

  public boolean canConvert (Class clazz)
  {

    return clazz.equals(Image.class);
  }

  @Override
  public void marshal (Object value, HierarchicalStreamWriter writer,
                       MarshallingContext context)
  {
    
    Image image = (Image) value;
    int height = image.getHeight();
    int width = image.getWidth();
    float scale = image.getScale();
    int rotation = image.getRotation();
    String ref = image.getRef();
    int horizontalOffset = image.getHorizontalOffset();
    int verticalOffset = image.getVerticalOffset();
    
    
    writer.startNode("height");
    writer.setValue(String.valueOf(height));
    writer.endNode();
    
    writer.startNode("width");
    writer.setValue(String.valueOf(width));
    writer.endNode();
    
    writer.startNode("scale");
    writer.setValue(String.valueOf(scale));
    writer.endNode();
    
    writer.startNode("rotation");
    writer.setValue(String.valueOf(rotation));
    writer.endNode();
    
    writer.startNode("ref");
    writer.setValue(ref);
    writer.endNode();
    
    writer.startNode("horizontalOffset");
    writer.setValue(String.valueOf(horizontalOffset));
    writer.endNode();
    
    writer.startNode("verticalOffset");
    writer.setValue(String.valueOf(verticalOffset));
    writer.endNode();
    
    

  }

  @Override
  public Object unmarshal (HierarchicalStreamReader reader,
                           UnmarshallingContext context)
  {
    reader.moveDown();
    int height = Integer.parseInt(reader.getValue());
    reader.moveUp();
    
    reader.moveDown();
    int width = Integer.parseInt(reader.getValue());
    reader.moveUp();
    
    reader.moveDown();
    float scale = Float.parseFloat(reader.getValue());
    reader.moveUp();
    
    reader.moveDown();
    int rotation = Integer.parseInt(reader.getValue());
    reader.moveUp();
    
    reader.moveDown();
    String ref = reader.getValue();
    reader.moveUp();
    
    reader.moveDown();
    int horizontalOffset = Integer.parseInt(reader.getValue());
    reader.moveUp();
    
    reader.moveDown();
    int verticalOffset = Integer.parseInt(reader.getValue());
    reader.moveUp();
    
    Image image = Image.getSubImage(Image.loadImageFromFile(ref), ref, width, height, horizontalOffset, verticalOffset);

    image.setScale(scale);
    image.setRotation(rotation);
    
    return image;
  }

}
