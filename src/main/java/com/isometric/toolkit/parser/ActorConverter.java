package com.isometric.toolkit.parser;

import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.NonPlayer;
import com.isometric.toolkit.entities.Player;
import com.isometric.toolkit.entities.Tile;
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
    writer.startNode("type");
    writer.setValue(String.valueOf(a.getType()));
    writer.endNode();
    
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
    
    writer.startNode("spritesheet");
    writer.setValue(String.valueOf(a.getSpriteSheet().getInternalImagePath()));
    writer.endNode();
    
    
  }

  @Override
  public Actor unmarshal (HierarchicalStreamReader reader,
                           UnmarshallingContext context)
  {
    
    reader.moveDown();
    String type = reader.getValue();
    reader.moveUp();
    
    Actor a = null;
    
    if(type.equalsIgnoreCase(Player.getType())){
      reader.moveDown();
      float x = Float.parseFloat(reader.getValue());
      reader.moveUp();
      
      reader.moveDown();
      float y = Float.parseFloat(reader.getValue());
      reader.moveUp();
      
       a = new Player(null, x, y);
      
    }else if (type.equalsIgnoreCase(NonPlayer.getType())){
      
    }else if (type.equalsIgnoreCase(Tile.getType())){
      
    }
    
    return a;
  }

}
