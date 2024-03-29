package com.isometric.toolkit.engine;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//import java.lang.Object;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.apache.commons.lang3.builder.*;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.cameras.Camera;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;
import com.isometric.toolkit.parser.WorldBuilder;
import com.isometric.toolkit.sound.SoundManager;



/***
 * World represents an entire game in the engine.
 * 
 * @author Jeff and Erik
 *
 */
public class World {
	static Logger logger = LoggerFactory.getLogger();

	private SoundManager soundManager = new SoundManager();
	private Map<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();
	private List<Actor> actors = new ArrayList<Actor>();
	private List<Level> levels = new ArrayList<Level>();
	private Level currentLevel = new Level();
	private Camera camera = null;
	private String worldName = "";
	private int spriteWidth = 20;
	private int spriteHeight = 20;
	
	
	//TODO - Hard coding this for now...
	public World()
	{
		this.spriteWidth = 22;//22
		this.spriteHeight = 25;//25
		
	}
	
	public void update(float delta) {
		

		camera.update(delta);

		currentLevel.update(delta);

	}

	public void draw() {

		// Clear the screen and depth buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glPushMatrix();
		// this.camera.applyTransform();
		// GL11.glTranslatef(100f, 100f, 0);
		// Apply camera transforms here!!!
		// GL11.glScalef(.5f, .5f, 0.f);
		camera.applyTransform();
		currentLevel.draw();
		GL11.glPopMatrix();

	}

	public void applyTransformations() {
		camera.applyTransform();
	}

	public void addActor(Actor a) {
		actors.add(a);
	}

	public void addLevel(Level l) {
		levels.add(l);
	}

	public void removeActor(Actor a) {
		actors.remove(a);
	}

	public void removeLevel(Level l) {
		levels.remove(l);
	}

	// TODO: Add custom exceptions! generic
	public void addSpriteSheet(String name, SpriteSheet ss) throws Exception {
		if (spriteSheets.containsKey(name)) {
			throw new Exception("Tried to add same name spritesheet twice!");
		}
		spriteSheets.put(name, ss);
	}

	public SpriteSheet getSpriteSheet(String name) {
		return spriteSheets.get(name);
	}

	public void removeSpriteSheet(String name) {
		spriteSheets.remove(name);
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public Level getCurrentLevel() {
		return currentLevel;
	}
	public List<Level> getLevels(){
	  return levels;
	  
	}

	public void setCurrentLevel(Level currentLevel) {
		this.currentLevel = currentLevel;
	}

	public List<Actor> getActors() {
		return this.actors;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	public void setSoundManager(SoundManager soundManager) {
		this.soundManager = soundManager;
	}
	
	public Map<String,SpriteSheet> getSpriteSheets(){
	  return this.spriteSheets;
	}
	
	@Override
	public String toString(){
	  return this.worldName;
	}


	public int getSpriteWidth() {
		return spriteWidth;
	}

	public void setSpriteWidth(int spriteWidth) {
		this.spriteWidth = spriteWidth;
	}
	
	public int getSpriteHeight() {
		return spriteHeight;
	}

	public void setSpriteHeight(int spriteHeight) {
		this.spriteHeight = spriteHeight;
	}
	
//	@Override
//	public boolean equals(Object obj) { 
//		//return true;
//	    return EqualsBuilder.reflectionEquals(this, obj); 
//	 } 
//
//	@Override
//	 public int hashCode() {
//		   return HashCodeBuilder.reflectionHashCode(this);
//		 }
	
	

}
