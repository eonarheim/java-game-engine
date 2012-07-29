package com.isometric.toolkit.parser;

import com.isometric.toolkit.sound.Sound;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SoundConverter implements Converter
{

  @Override
  public boolean canConvert (Class clazz)
  {
    // TODO Auto-generated method stub
    return clazz.equals(Sound.class);
  }

  @Override
  public void marshal (Object value, HierarchicalStreamWriter writer,
                       MarshallingContext context)
  {
    // TODO Auto-generated method stub
    Sound s = (Sound) value;
    
    writer.startNode("soundPath");
    writer.setValue(s.getSoundPath());
    writer.endNode();
    

  }

  @Override
  public Object unmarshal (HierarchicalStreamReader reader,
                           UnmarshallingContext context)
  {
    // TODO Auto-generated method stub
    
    reader.moveDown();
    String path = reader.getValue();
    reader.moveUp();
    
    Sound result = new Sound(path);
    return result;
  }

}
