package com.isometric.toolkit.engine;

import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.parser.WorldBuilder;

/***
 * Window container for the engine that handles high level concerns.
 * 
 * @author Jeff and Erik
 * 
 */
public class Window {

	static Logger logger = LoggerFactory.getLogger();
	Vector<Integer> keyPress = new Vector<Integer>();

	private static List<String> debugList = new ArrayList<String>();
	private static List<Integer> timerList = new ArrayList<Integer>();
	
	private int width = 800;
	private int height = 600;

	private World gameWorld = null;
	private boolean calledInit = false;
	private static boolean debug = true;
	private static boolean console = false;

	private Console c = null;

	private Font font = new Font("HELVETICA", Font.PLAIN, 15);
	private UnicodeFont f = new UnicodeFont(font);

	public static Integer currentKey = null;

	private long lastFrame;

	private int fps;
	private int finalFps;
	private int ticker;

	private long lastFPS;

	public Window(int width, int height, boolean fullscreen) {
	  

		try {
			logger.info("Creating LWJGL display");

			if(fullscreen){
			  Display.setFullscreen(true);
			  this.width = Display.getWidth();
			  this.height = Display.getHeight();
			}else{
			  Display.setDisplayMode(new DisplayMode(width, height));
			}
			
			Display.create();
			Display.setTitle("Java RPG Toolkit");

			f.addAsciiGlyphs();
			f.getEffects().add(new ColorEffect(java.awt.Color.YELLOW));
			f.loadGlyphs();

			logger.info("Loading console");
			c = new Console();

		} catch (LWJGLException e) {
			logger.error("Could not create LWJGL display! Exiting");
			e.printStackTrace();
			System.exit(1);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	

	public void init(World w) {
		logger.info("Window initialized");
		this.calledInit = true;
		this.gameWorld = w;
		Console.setWorld(w);

	}

	public void start() {
		if (!calledInit) {
			logger.error("Did not call init before start!");
			System.exit(1);
		}

		// init OpenGL
		logger.info("Initializing opengl..");
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glViewport(0, 0, width, height);
		GL11.glClearColor(0f, 0f, .0f, 1f);

		logger.info("Entering mainloop");

		// com.isometric.toolkit.editor.Editor foo = new
		// com.isometric.toolkit.editor.Editor();
		// com.isometric.toolkit.editor.Editor.start();

		float delta = getDelta(); // call once before loop to initialise
									// lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		while (!Display.isCloseRequested()
				&& !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {

			updateFPS();
			if (!isConsole()) {
				gameWorld.update(getDelta());
			}
			gameWorld.draw();
			// Debug text
			if (isDebug()) {
				float yoffset = 25.f;
				GL11.glEnable(GL11.GL_BLEND);
				f.drawString(10f, 10f, "FPS: " + finalFps);
				f.drawString(80f, 10f, "Memory Usage: "
						+ (Runtime.getRuntime().totalMemory() / 1024 - Runtime
								.getRuntime().freeMemory() / 1024) + "K");
				f.drawString(250f, 10f, "CPUs: "
						+ Runtime.getRuntime().availableProcessors());
				for (String s : debugList) {
					f.drawString(10f, yoffset, s);
					yoffset += 15.f;
				}
				GL11.glDisable(GL11.GL_BLEND);

			}

			checkInput();

			if (isConsole()) {
				// c.update();
				c.draw();
			}
			//Display.setVSyncEnabled(true);
			Display.update();
			Display.sync(60);
			

		}

		Display.destroy();
		logger.info("Close requested ... Display destroyed");

	}

	public long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public void tick() {

		if (ticker++ / 60.f > 1.f) {
			for (int i = 0; i < timerList.size(); i++) {
				Integer tmp = timerList.get(i);
				timerList.set(i, tmp - 1);
				if (tmp <= 0) {
					timerList.remove(i);
					debugList.remove(i);
				}
			}
			ticker = 0;
		}
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		tick();
		if (getTime() - lastFPS > 1000) {
			// Display.setTitle("FPS: " + fps);
			finalFps = fps;
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	public float getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta / 1000f;
	}

	public void checkInput() {

		while (Keyboard.next()) {

			c.update(Keyboard.getEventCharacter());
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
					Window.writeToDebug("Serializing world now!");
					logger.info("Serializing world...");

					// Attempt to write world to xml file.
					// Relies on format of *world_x.* where * can be anything
					// not containing '_' or '.' and x is an int
					try {
						System.out.println("Working Directory = "
								+ System.getProperty("user.dir"));

						File folder = new File(System.getProperty("user.dir")
								+ "/src/main/resources/worlds/");
						int fileNum = 0;
						for (File file : folder.listFiles()) {
							String fileName = file.getName();

							if (fileName.contains("world_")) {
								int fileNumber = Integer.parseInt(fileName
										.substring(fileName.indexOf('_') + 1,
												fileName.indexOf('.')));
								if (fileNumber > fileNum)
									fileNum = fileNumber;
							}

						}
						WorldBuilder.writeWorldToFile(gameWorld,
								System.getProperty("user.dir")
										+ "/src/main/resources/worlds/world_"
										+ String.valueOf(++fileNum) + ".xml");
					} catch (Exception e) {
						logger.info("Serialization of world failed. Exception: "
								+ e.toString()
								+ ". Message:  "
								+ e.getMessage());
					}
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_D && !isConsole()) {
					if (!isDebug()) {
						System.out.println("Debug Enabled");
						logger.info("Debug enabled");
						setDebug(true);
					} else {
						System.out.println("Debug Disabled");
						logger.info("Debug Disabled");
						setDebug(false);
					}
				}

				if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
					if (!isConsole()) {
						Window.writeToDebug("Jython Console Active");
						setConsole(true);
					} else {
						Window.writeToDebug("Jython Console Disabled");
						setConsole(false);
					}
				}

			} else {
				if (Keyboard.getEventKey() == Keyboard.KEY_D) {
				}
			}
		}
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean b) {
		debug = b;
	}

	public static void writeToDebug(String msg) {
		debugList.add(msg);
		timerList.add(3);
	}

	public static boolean isConsole() {
		return console;
	}

	public static void setConsole(boolean console) {
		Window.console = console;
	}

}
