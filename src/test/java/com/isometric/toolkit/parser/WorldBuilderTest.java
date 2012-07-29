package com.isometric.toolkit.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;

public class WorldBuilderTest {
	
	@Before
	public void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(1, 1));
			Display.create();
			Display.setTitle("Running Tests");
		} catch (LWJGLException e) {
			fail("Failed to create Display");
		}
	}

	@After
	public void destroyDisplay() {
		Display.destroy();
	}

	@Test
	public void test() {
		World w = new World();
		w.setWorldName("Default World");
		SpriteSheet ss = new SpriteSheet("TestPlayer.png", 10, 1);
		fail("Not yet implemented"); // TODO
	}

}
