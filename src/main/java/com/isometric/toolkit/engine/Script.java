package com.isometric.toolkit.engine;

import java.util.ArrayList;
import java.util.List;

/***
 * Script class to encapsulate python scripts for the interpreter. 
 * 
 * @author Erik
 *
 */
public class Script
{
  private String scriptText;
  private List<String> scriptLines = new ArrayList<String>();
  
  public Script(String scriptText){
    this.scriptText = scriptText;
    load(scriptText);
  }
  
  /***
   * Runs the specified script in the global interpreter
   */
  public void run(){
    Interpreter i = Interpreter.getInterpreter();
    i.run(this);
    load(this.scriptText);
  }
  
  /***
   * This will return the raw text of the Python script to be run on the interpreter
   * @return String rawScript
   */
  public String getScriptText(){
    return this.scriptText;
  }
  
  /***
   * This will return the next line in the script an remove it from the internal representation
   * @return
   */
  public String getLine(){
    return this.scriptLines.remove(0);
  }
  
  /***
   * Indicates whether or not there are lines left in the script.
   * @return
   */
  public boolean hasLines(){
    return this.scriptLines.size()>0;
  }
  
  /***
   * Indicates the number of lines left in the script.
   * @return
   */
  public int numLines(){
    return this.scriptLines.size();    
  }
  
  /***
   * Populates the internal structure to run the script again
   * @param scriptText
   */
  private void load(String scriptText){
    String[] tmpList = scriptText.split("\n");
    for(String s : tmpList){
      this.scriptLines.add(s);      
    }
  }
  

}
