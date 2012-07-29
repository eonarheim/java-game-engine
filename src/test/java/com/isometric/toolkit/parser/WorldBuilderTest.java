package com.isometric.toolkit.parser;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.World;

public class WorldBuilderTest {

	//http://maven.40175.n5.nabble.com/M2-classpath-problem-with-surefire-td42596.html
	
	@Test
	public void test() {
		World w = new World();
		w.setWorldName("Default World");
		// this.getClass().getClassLoader().getSystemResourceAsStream("images/TestPlayer.png");
		InputStream foo = this.getClass().getClassLoader()
				.getSystemResourceAsStream("images/TestPlayer.png");
		if (foo == null)
			fail("Foo equals null");
//		else
//			fail("Foo wasn't null!");

		SpriteSheet ss = new SpriteSheet("TestPlayer.png", 10, 1, foo);
		fail("Not yet implemented"); // TODO
	}

}
