package com.isometric.toolkit.parser;

import com.isometric.toolkit.entities.Actor;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ActorConverter implements Converter
{

  
  public boolean canConvert (Class clazz)
  {
    return clazz.equals(Actor.class);
  }


  public void marshal (Object value, HierarchicalStreamWriter writer,
                       MarshallingContext context)
  {
    Actor a = (Actor) value;
    writer.startNode("x");
    writer.setValue(String.valueOf(a.getX()));
    writer.endNode();
    
    writer.startNode("y");
    writer.setValue(String.valueOf(a.getY()));
    writer.endNode();
    
    writer.startNode("dx");
    writer.setValue(String.valueOf(a.getDx()));
    writer.endNode();
    
    writer.startNode("dy");
    writer.setValue(String.valueOf(a.getDy()));
    writer.endNode();
    
    
  }

  @Override
  public Object unmarshal (HierarchicalStreamReader arg0,
                           UnmarshallingContext arg1)
  {
    // TODO Auto-generated method stub
    return null;
  }

}
