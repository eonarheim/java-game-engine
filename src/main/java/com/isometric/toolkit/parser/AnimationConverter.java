package com.isometric.toolkit.parser;

import com.isometric.toolkit.engine.Animation;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AnimationConverter implements Converter
{

  
  public boolean canConvert (Class clazz)
  {
    return clazz.equals(Animation.class);
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
