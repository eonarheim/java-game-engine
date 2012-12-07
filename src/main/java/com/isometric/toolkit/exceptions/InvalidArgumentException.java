package com.isometric.toolkit.exceptions;

/***
 * Thown when arguments are invalid for any reason
 * 
 * @author Jeff
 * 
 */
public class InvalidArgumentException extends Exception
{
  
  public InvalidArgumentException(String message){
    super(message);
  }

}
