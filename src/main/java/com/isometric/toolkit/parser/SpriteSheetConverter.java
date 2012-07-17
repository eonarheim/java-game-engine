package com.isometric.toolkit.parser;

import com.isometric.toolkit.engine.Image;
import com.isometric.toolkit.engine.SpriteSheet;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SpriteSheetConverter implements Converter
{

  
  public boolean canConvert (Class clazz)
  {
  
    return clazz.equals(SpriteSheet.class);
  }

  
  public void marshal (Object value, HierarchicalStreamWriter writer,
                       MarshallingContext context)
  {
    SpriteSheet ss = (SpriteSheet) value;
    writer.startNode("ref");
    writer.setValue(String.valueOf(ss.getInternalImagePath()));
    writer.endNode();
    
    writer.startNode("horizontalCount");
    writer.setValue(String.valueOf(ss.getHorizontalCount()));
    writer.endNode();
    
    writer.startNode("verticalCount");
    writer.setValue(String.valueOf(ss.getVerticalCount()));
    writer.endNode();

  }

  
  public Object unmarshal (HierarchicalStreamReader reader,
                           UnmarshallingContext context)
  {
    
    reader.moveDown();
    String ref = reader.getValue();
    reader.moveUp();
    
    reader.moveDown();
    int horizontalCount = Integer.parseInt(reader.getValue());
    reader.moveUp();
    
    reader.moveDown();
    int verticalCount = Integer.parseInt(reader.getValue());
    reader.moveUp();
    
    SpriteSheet ss = new SpriteSheet(ref, horizontalCount, verticalCount);
    
    return ss;
  }

}
