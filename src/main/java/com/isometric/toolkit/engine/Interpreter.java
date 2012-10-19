package com.isometric.toolkit.engine;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;
import org.python.util.PythonInterpreter;

import com.isometric.toolkit.LoggerFactory;

/***
 * Python interpreter for the engine to run as a singleton to persist state across Scripts.
 * 
 * @author Erik
 *
 */
public class Interpreter
{
  private static Logger logger = LoggerFactory.getLogger();
  //private ByteArrayOutputStream interpreterOut = new  ByteArrayOutputStream();
  protected PythonInterpreter interpreter = null;
  private static Interpreter singleton;
  
  
  // Factory pattern to prevent multiple instances of the interpreter from being created.
  private Interpreter(){

    interpreter = new PythonInterpreter();
    interpreter.exec("from com.isometric.toolkit.actions import *");
    interpreter.exec("from com.isometric.toolkit.entities import *");
  }
  
  /***
   * This method returns the global instance of the interpreter
   * @return Interpreter
   */
  public static Interpreter getInterpreter(){
    if(singleton == null){
      logger.info("Creating instance of Python Interpreter...");
      singleton = new Interpreter();
    }
    return singleton;
  }
  
  /***
   * Runs a particular script in the interpreter. Global variables declared will persist in the interpreter.
   * @param script
   */
  public void run(Script script){
    while(script.hasLines()){
      //interpreter.setOut(interpreterOut);
      try{
        interpreter.eval(script.getLine());
        
        //Window.writeToDebug(interpreterOut.toString());
      }catch (Exception e){
        logger.error("Jython Error: " + e.getMessage());
      }
    }
    
  }
  
}
