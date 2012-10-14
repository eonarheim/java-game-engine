package com.isometric.toolkit.parser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.actions.Action;
import com.isometric.toolkit.actions.MoveTo;
import com.isometric.toolkit.actions.RepeatForever;
import com.isometric.toolkit.cameras.Camera;
import com.isometric.toolkit.engine.Animation;
import com.isometric.toolkit.engine.Image;
import com.isometric.toolkit.engine.KeyCombo;
import com.isometric.toolkit.engine.Motion;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.engine.SpriteSheet;
import com.isometric.toolkit.engine.Texture;
import com.isometric.toolkit.engine.Trigger;
import com.isometric.toolkit.engine.World;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.entities.Level;
import com.isometric.toolkit.entities.NonPlayer;
import com.isometric.toolkit.entities.Player;
import com.isometric.toolkit.entities.Tile;
import com.isometric.toolkit.sound.Sound;
import com.isometric.toolkit.sound.SoundManager;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

//import com.isometric.toolkit.entities.Playable;

public class WorldBuilder {

	static Logger logger = LoggerFactory.getLogger();

	public static World newWorld() throws Exception {
		World w = new World();

		w.setWorldName("Default World");
		SpriteSheet ss = new SpriteSheet("TestPlayer.png", 10, 1);
		ss.createAnimation(1, 2, "walkDown", 0.2f); // todo, left off: pass in
													// an Animation name, use
													// that to reference it in
													// the future.
		ss.createAnimation(3, 4, "walkLeft", 0.2f); // erik says an actor can
													// reference a
													// spritesheet(s) and just
													// say "walkUp" is your
													// animation for walking up
		ss.createAnimation(6, 7, "walkUp", 0.2f); // and it'll go get the
													// animation of that name
													// from the spritesheet
		ss.createAnimation(8, 9, "walkRight", 0.2f);
		w.addSpriteSheet("TestPlayer", ss);

		SpriteSheet tiles = new SpriteSheet("BasicTileSet.png", 10, 2);
		tiles.createAnimation(0, 1, "grass", 1.f);
		w.addSpriteSheet("BasicTileSet", tiles);

		Player player = new Player(w, 0, 0);
		player.setSpriteSheetName("TestPlayer");
		//player.setHeight(25.f);
		//player.setWidth(25.f);
		w.addActor(player);

		Player testCollide = new Player(w, 100, 100);
		testCollide.setSpriteSheetName("TestPlayer");
		testCollide.addAnimation("walkUp");
		testCollide.addAnimation("walkDown");
		//testCollide.setHeight(35.f);
		//testCollide.setWidth(35.f);

		testCollide.setScale(2.f);

		w.addActor(testCollide);

		Camera c = new Camera(player, 200f, 800, 600);
		w.setCamera(c);

		Sound s = new Sound("sonicring.wav");

		w.getSoundManager().addSound("ring", s);
		player.addAnimation("walkDown");
		player.addKeyHook(Keyboard.KEY_DOWN, "walkDown");
		player.addMotionHook(new KeyCombo(Keyboard.KEY_DOWN, null), new Motion(
				0.f, 1.f));

		player.addAnimation("walkLeft");
		player.addKeyHook(Keyboard.KEY_RIGHT, "walkLeft");
		player.addMotionHook(new KeyCombo(Keyboard.KEY_RIGHT, null),
				new Motion(1.f, 0.f));

		player.addAnimation("walkUp");
		player.addKeyHook(Keyboard.KEY_UP, "walkUp");
		player.addMotionHook(new KeyCombo(Keyboard.KEY_UP, null), new Motion(
				0.f, -1.f));

		player.addAnimation("walkRight");
		player.addKeyHook(Keyboard.KEY_LEFT, "walkRight");
		player.addMotionHook(new KeyCombo(Keyboard.KEY_LEFT, null), new Motion(
				-1.f, 0.f));

		player.setCurrentAnimation("walkDown");
		player.setScale(2.0f);

		Level newLevel = new Level();
		newLevel.addActor(player);
		
		
		Action move = new MoveTo(player, new Point(500,0), 40.f);
		Action moveDown = new MoveTo(player, new Point(500,500),40.f);
		Action moveLeft = new MoveTo(player, new Point(0,500),40.f);
		Action moveUp = new MoveTo(player, new Point(0,0),40.f);
		RepeatForever moveForever = new RepeatForever(player);
		moveForever.addAction(move);
		moveForever.addAction(moveDown);
		moveForever.addAction(moveLeft);
		moveForever.addAction(moveUp);
		player.addAction(moveForever);

		for (int x = 0; x < 20; x++) {
			for (int y = 0; y < 20; y++) {
				Tile tile = new Tile(w, false, 0, 0);
				tile.setSpriteSheetName("BasicTileSet");
				tile.addAnimation("grass");
				tile.setCurrentAnimation("grass");
				tile.setScale(2.0f);
				tile.setX(x * 22 * 2.0f);
				tile.setY(y * 24 * 2.0f);
				newLevel.addTile(tile);
			}
		}

		newLevel.addActor(testCollide);
		w.addLevel(newLevel);
		w.setCurrentLevel(newLevel);

		return w;
	}
	
	public static World parseWorld(String xml) throws Exception {
		World worldResult = new World();

		logger.info("Reading world file");

		XStream x = setup();

		try{
		  worldResult = (World) x.fromXML(xml);
		  logger.info("Parsing world successful");
	                
	          return worldResult;
		}catch(Exception e){
		  logger.error("Could not parse world file: " + e.getMessage() );
		  
		  throw e;
		}
		
	}

	public static World parseWorldFromFile(String uri) {
		World worldResult = new World();

		logger.info("Reading world file");

		XStream x = setup();

		worldResult = (World) x.fromXML(WorldBuilder.class.getClassLoader()
				.getResourceAsStream(uri));

		/*
		 * Properties WorldProps = new Properties();
		 * 
		 * 
		 * 
		 * try { logger.info("Loading world file: " + uri);
		 * WorldProps.load(WorldBuilder
		 * .class.getClassLoader().getResourceAsStream(uri));
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * logger.error("Could not load world file: " + uri);
		 * e.printStackTrace(); }
		 * 
		 * World w = new World();
		 * w.setWorldName(WorldProps.getProperty("WorldName")); String
		 * playableActor = "actors/"+WorldProps.getProperty("Playable");
		 * 
		 * Properties playableProps = new Properties();
		 * 
		 * try { logger.info("Loading playable actor file: " + playableActor);
		 * playableProps
		 * .load(WorldBuilder.class.getClassLoader().getResourceAsStream
		 * (playableActor));
		 * 
		 * } catch (IOException e) { // TODO Auto-generated catch block
		 * logger.error("Could not load actor file: " + playableActor);
		 * e.printStackTrace(); }
		 * 
		 * 
		 * Actor player = null;
		 * 
		 * try { logger.info("Creating object of type: " +
		 * playableProps.getProperty("Class")); Class c =
		 * Class.forName(playableProps.getProperty("Class")); Constructor cons =
		 * c.getDeclaredConstructor(new Class[]{String.class, int.class,
		 * int.class, float.class, float.class, float.class, float.class});
		 * 
		 * playableProps.remove("Class");
		 * 
		 * player = (Actor)
		 * cons.newInstance((String)playableProps.get("SpriteRef"),
		 * Integer.parseInt((String) playableProps.get("Height")),
		 * Integer.parseInt((String) playableProps.get("Width")),
		 * Float.parseFloat((String) playableProps.get("X")),
		 * Float.parseFloat((String) playableProps.get("Y")),
		 * Float.parseFloat((String) playableProps.get("Dx")),
		 * Float.parseFloat((String) playableProps.get("Dy")));
		 * 
		 * 
		 * Level newLevel = new Level(); newLevel.addActor(player);
		 * w.addLevel(newLevel); w.setCurrentLevel(newLevel); } catch (Exception
		 * e){ logger.error("Error creating object of type: " +
		 * playableProps.getProperty("Class")); e.printStackTrace(); }
		 */

		return worldResult;
	}

	public static void writeWorldToLogger(World w) {

		logger.info("Serializing world file to logger");		
		logger.info("World To XML: \n" + serializeWorld(w));
	}

	public static String writeWorld(World w) {

		logger.info("Serializing world file");
		logger.info(serializeWorld(w));
		return serializeWorld(w);
	}

	public static String serializeWorld(World w) {
		XStream x = setup();
		return x.toXML(w);
	}

	public static Level parseLevel(String xml) {

		return new Level();
	}

	public static Player parsePlayer(String xml) {

		return new Player(null, 0, 0);
	}

	public static NonPlayer parseNonPlayer(String xml) {
		return new NonPlayer(null, 0f, 0f, 0f, 0f);
	}

	public static Trigger parseTrigger(String xml) {
		return new Trigger();
	}

	private static XStream setup() {

		XStream x = new XStream(new DomDriver());
		x.processAnnotations(Player.class);
		x.autodetectAnnotations(true);
		// World Alias's
		x.alias("world", World.class);
		x.alias("actor", Actor.class);
		//x.alias("player", Player.class);
		x.alias("level", Level.class);
		x.alias("trigger", Trigger.class);
		x.alias("tile", Tile.class);
		x.alias("nonPlayer", NonPlayer.class);
		x.alias("texture", Texture.class);
		x.alias("motion", Motion.class);
		x.alias("keyCombo", KeyCombo.class);
		x.alias("image", Image.class);
		x.alias("sound", Sound.class);
		x.alias("animation", Animation.class);
		x.alias("spriteSheet", SpriteSheet.class);

		/*
		 * x.addImplicitCollection(World.class,"actors");
		 * x.addImplicitCollection(World.class, "levels");
		 * x.addImplicitCollection(Level.class, "backgroundLayer");
		 * x.addImplicitCollection(Level.class, "objectLayer");
		 * x.addImplicitCollection(Level.class, "foregroundLayer");
		 */
		// x.registerConverter(new TileConverter());
		x.registerConverter(new ImageConverter());
		x.registerConverter(new SoundConverter());
		// x.registerConverter(new SpriteSheetConverter());

		return x;
	}

}
