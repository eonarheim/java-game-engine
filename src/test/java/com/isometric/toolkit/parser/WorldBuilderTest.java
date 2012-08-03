package com.isometric.toolkit.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.isometric.toolkit.cameras.Camera;
import com.isometric.toolkit.engine.*;
import com.isometric.toolkit.entities.*;
import com.isometric.toolkit.sound.*;

public class WorldBuilderTest {
	
	private World w = null;
	
	@BeforeClass
	public static void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(1, 1));
			Display.create();
			Display.setTitle("Running Tests");
		} catch (LWJGLException e) {
			fail("Failed to create Display");
		}
	}

	@AfterClass
	public static void destroyDisplay() {
		Display.destroy();
	}

	@Test
	public void test() {
        //So to test this stuff,  I could build a world programatically, serialize it, deserialize it, then compare. That would
        //be testing both ser/des of course, which isn't very unit test like.
        //I could use setup conditions to set my stuff, and then test it that way.
       
        //2 objectives:
        //Make sure old versions are compatible
        //Make sure serialization works
             //Pretty sure the best way is to serialize and then deserialize. Right now, when you add a property to a class (at least one that doesn't
             //have a converter), it'll automatically get serialized and deserialized. I think since this automation is in there, it is fine if we test
             //those two at once, that way we don't have to create xml files that will become stale (that part is for the version compatibility!)
		fail("Not yet implemented comletely"); // TODO
		String worldFile = WorldBuilder.writeWorld(w);		
		World ww = WorldBuilder.parseWorld(worldFile);
		String worldFile2 = WorldBuilder.writeWorld(ww);
		if (ww.equals(w))
		{
			Assert.assertTrue(true);//("Hell yeah, hell yeah!";)
		}
		else
		{
			fail("Yep, not equal");
		}
		//assertEquals(w, ww);
		//assert(w.equals(ww));		
		//assertEquals(worldFile, worldFile2);
   }
  
   /**
   * Note: Don't need to test Getters/Settings. Following the philosophy
   * "test everything which can possibly break" (reading between the lines: "don't test trivial stuff")
   * 
   * Currently this has a lot of dependencies. Calling this an integration test at the moment, so
   * I'm not dealing with it.
   */
   @Before
   public void createTestWorld()
   {
	   try {
		w = WorldBuilder.newWorld();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		fail("Exception occured. Investigate");
		e.printStackTrace();
	}
   }

}
