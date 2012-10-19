package com.isometric.toolkit.exceptions;

/***
 * Thrown when and action occurs after a RepeatForever action in any action queue.
 * 
 * @author Erik
 *
 */
public class RepeatForeverException extends Exception
{
  public RepeatForeverException(String message){
    super(message);
  }
  
}
