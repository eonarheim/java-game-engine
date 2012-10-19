package com.isometric.toolkit;

import org.apache.log4j.Logger;

/***
 * Logger factor returns the proper log4j logger for the calling class.
 * 
 * @author Erik
 *
 */
public class LoggerFactory
{
  /***
   * Logger for the calling class.
   * @return Log4j Logger
   */
  public static Logger getLogger(){
    
    return  Logger.getLogger(new Throwable().getStackTrace()[1].getClassName());
    
  }
}
