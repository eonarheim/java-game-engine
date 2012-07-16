package com.isometric.toolkit.parser;

import com.isometric.toolkit.entities.Level;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class LevelConverter implements Converter
{

  
  public boolean canConvert (Class clazz)
  {
    
  
    return clazz.equals(Level.class);
  }

  @Override
  public void marshal (Object value, HierarchicalStreamWriter writer,
                       MarshallingContext context)
  {
    // TODO Auto-generated method stub

  }

  @Override
  public Object unmarshal (HierarchicalStreamReader reader,
                           UnmarshallingContext context)
  {
    // TODO Auto-generated method stub
    
    
    return null;
  }

}
