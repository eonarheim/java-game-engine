package com.isometric.toolkit.parser;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SpriteSheetConverter implements Converter
{

  
  public boolean canConvert (Class clazz)
  {
  
    return false;
  }

  
  public void marshal (Object value, HierarchicalStreamWriter writer,
                       MarshallingContext context)
  {
  

  }

  
  public Object unmarshal (HierarchicalStreamReader reader,
                           UnmarshallingContext context)
  {
      return null;
  }

}
