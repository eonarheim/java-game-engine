package com.isometric.toolkit.engine;


public class KeyCombo implements Comparable<KeyCombo>
{
  private Integer key1;
  private Integer key2;

  public KeyCombo (Integer key1, Integer key2)
  {
    this.setKey1(key1);
    this.setKey2(key2);

  }

  @Override
  public int compareTo (KeyCombo other)
  {
    if (other.getKey1() == this.getKey1()
        && other.getKey2() == this.getKey2()) {
      // TODO Auto-generated method stub
      return 0;
    }
    else {
      return -1;
    }
  }

  public Integer getKey1 ()
  {
    return key1;
  }

  public void setKey1 (Integer key1)
  {
    this.key1 = key1;
  }

  public Integer getKey2 ()
  {
    return key2;
  }

  public void setKey2 (Integer key2)
  {
    this.key2 = key2;
  }

}
