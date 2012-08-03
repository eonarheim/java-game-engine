/**
 * 
 */
package com.isometric.toolkit.engine;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jeff Brock
 *
 */
public class KeyComboTest {
	private static Random generator;
	
	@BeforeClass
	public static void SetupRandomGenerator()
	{
		generator = new Random();
	}

	/**
	 * Test method for {@link com.isometric.toolkit.engine.KeyCombo#KeyCombo(java.lang.Integer, java.lang.Integer)}.
	 */
	@Test
	public void testKeyCombo() {		
		KeyCombo k = new KeyCombo(0, 0);
		assertNotNull(k);
		
		KeyCombo k2 = new KeyCombo(null, null);
		assertNotNull(k2);
	}

	/**
	 * Test method for {@link com.isometric.toolkit.engine.KeyCombo#compareTo(com.isometric.toolkit.engine.KeyCombo)}.
	 */
	@Test
	public void testCompareTo() {
		KeyCombo k = new KeyCombo(1, 1);
		KeyCombo k2 = new KeyCombo(1, 1);
		KeyCombo k3 = new KeyCombo(1, 2);
		KeyCombo k4 = new KeyCombo(2, 1);
		assertEquals(k.compareTo(k2), 0);
		assertEquals(k.compareTo(k3), -1);
		assertEquals(k.compareTo(k4), -1);
		
		KeyCombo k5 = new KeyCombo(null, null);
		KeyCombo k6 = new KeyCombo(null, null);
		KeyCombo k7 = new KeyCombo(1, null);
		KeyCombo k8 = new KeyCombo(null, 1);
		assertEquals(k5.compareTo(k6), 0);
		assertEquals(k5.compareTo(k7), -1);
		assertEquals(k5.compareTo(k8), -1);		
	}

	/**
	 * Test method for {@link com.isometric.toolkit.engine.KeyCombo#getKey1()}.
	 * Test when Key1 is null and not null.
	 */
	@Test
	public void testGetKey1() {
		Integer r = generator.nextInt();
		KeyCombo k = new KeyCombo(r, r-1);
		assertEquals(k.getKey1(), r);
		
		KeyCombo k2 = new KeyCombo(null, 0);
		assertNull(k2.getKey1());
	}

	/**
	 * Test method for {@link com.isometric.toolkit.engine.KeyCombo#setKey1(java.lang.Integer)}.
	 */
	@Test
	public void testSetKey1() {
		Integer r = generator.nextInt();
		KeyCombo k = new KeyCombo(0, 0);
		k.setKey1(r);
		assertEquals(k.getKey1(), r);
		
		KeyCombo k2 = new KeyCombo(0, 0);
		k2.setKey1(null);
		assertNull(k2.getKey1());
		
		KeyCombo k3 = new KeyCombo(null, 0);
		k3.setKey1(r);
		assertEquals(k3.getKey1(), r);
	}

	/**
	 * Test method for {@link com.isometric.toolkit.engine.KeyCombo#getKey2()}.
	 * Test when Key1 is null and not null.
	 */
	@Test
	public void testGetKey2() {
		Integer r = generator.nextInt();
		KeyCombo k = new KeyCombo(r-1, r);
		assertEquals(k.getKey2(), r);
		
		KeyCombo k2 = new KeyCombo(0, null);
		assertNull(k2.getKey2());
	}

	/**
	 * Test method for {@link com.isometric.toolkit.engine.KeyCombo#setKey2(java.lang.Integer)}.
	 */
	@Test
	public void testSetKey2() {
		Integer r = generator.nextInt();
		KeyCombo k = new KeyCombo(0, 0);
		k.setKey2(r);
		assertEquals(k.getKey2(), r);
		
		KeyCombo k2 = new KeyCombo(0, 0);
		k2.setKey2(null);
		assertNull(k2.getKey2());
		
		KeyCombo k3 = new KeyCombo(null, 0);
		k3.setKey2(r);
		assertEquals(k3.getKey2(), r);
	}

}
