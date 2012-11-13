package com.isometric.toolkit.engine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScriptLoader
{
  private static Map<String,String> scriptCache = new HashMap<String,String>();
  
  public static Script LoadScript(String filePath){
    
    if(scriptCache.containsKey(filePath)){
      return new Script(scriptCache.get(filePath));
    }else{
      StringBuilder sb = new StringBuilder();
      try {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        String line;
        while((line = br.readLine()) != null){
          sb.append(line);
        }
        scriptCache.put(filePath, sb.toString());
        return new Script(scriptCache.get(filePath));
        
      }
      catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }   
    }
    return null;
  }
}
