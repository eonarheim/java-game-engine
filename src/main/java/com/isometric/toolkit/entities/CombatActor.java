package com.isometric.toolkit.entities;

import java.util.ArrayList;
import java.util.List;

import com.isometric.toolkit.engine.World;

public abstract class CombatActor extends Actor
{

  // Stats
  private float health = 0.0f;
  private float mana = 0.0f;
  private float attack = 0.0f;
  private float defense = 0.0f;

  private List<Item> inventory = new ArrayList<Item>();

  protected CombatActor (World w, float x, float y)
  {
    super(w, x, y, 0, 0);
    // TODO Auto-generated constructor stub
  }

  public abstract void draw ();

  public abstract boolean collides (Actor a);

  public float getHealth ()
  {
    return health;
  }

  public void setHealth (float health)
  {
    this.health = health;
  }

  public float getMana ()
  {
    return mana;
  }

  public void setMana (float mana)
  {
    this.mana = mana;
  }

  public List<Item> getInventory ()
  {
    return inventory;
  }

  public void setInventory (List<Item> inventory)
  {
    this.inventory = inventory;
  }

  public void addItem (Item i)
  {

    this.inventory.add(i);
  }

  public Item getItem (int i)
  {
    return this.inventory.get(i);
  }

  public Item removeItem (int i)
  {
    return this.inventory.remove(i);
  }

  public float getDefense ()
  {
    return defense;
  }

  public void setDefense (float defense)
  {
    this.defense = defense;
  }

  public float getAttack ()
  {
    return attack;
  }

  public void setAttack (float attack)
  {
    this.attack = attack;
  }
  
  
  
}
