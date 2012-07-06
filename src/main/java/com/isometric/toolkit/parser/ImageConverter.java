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
  public void marshal (Object object, HierarchicalStreamWriter writer,
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
