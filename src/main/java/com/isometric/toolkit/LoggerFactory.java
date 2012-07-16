package com.isometric.toolkit;

import org.apache.log4j.Logger;

public class LoggerFactory
{
  public static Logger getLogger(){
    
    return  Logger.getLogger(new Throwable().getStackTrace()[1].getClassName());
    
  }
}
