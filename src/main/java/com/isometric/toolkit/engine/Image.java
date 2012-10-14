package com.isometric.toolkit.engine;

import static org.junit.Assert.fail;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;

import com.isometric.toolkit.LoggerFactory;
import com.isometric.toolkit.exceptions.MissingSpriteSheetException;

//temp: Jeff
import java.net.URL;
import java.net.URLClassLoader;

public class Image implements Drawable {

	static Logger logger = LoggerFactory.getLogger();
	private Texture texture;
	private BufferedImage bufferedImage;
	private float scale = 1.f;
	// in degrees
	private int rotation = 0;
	private int width = 0;
	private int height = 0;

	// things for serialization
	private String ref = "";
	private int horizontalOffset;
	private int verticalOffset;

	// OpenGL Stuff
	private static ComponentColorModel glAlphaColorModel = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 8 }, true, false,
			ComponentColorModel.TRANSLUCENT, DataBuffer.TYPE_BYTE);

	private static ComponentColorModel glColorModel = new ComponentColorModel(
			ColorSpace.getInstance(ColorSpace.CS_sRGB),
			new int[] { 8, 8, 8, 0 }, false, false, ComponentColorModel.OPAQUE,
			DataBuffer.TYPE_BYTE);

	private static IntBuffer textureIDBuffer = BufferUtils.createIntBuffer(1);

	public Image() {

	}

	public Image(Texture texture) {
		this.texture = texture;
	}

	/***
	 * Returns a subsection of a BufferedImage
	 * 
	 * @param spriteSheetImage
	 * @param width
	 * @param height
	 * @param horizontalOffset
	 * @param verticalOffset
	 * @return
	 */
	public static BufferedImage getSubImage(BufferedImage spriteSheetImage,
			int width, int height, int horizontalOffset, int verticalOffset) {
		logger.info("Loading sprite. Vertical Offset(" + height + "): "
				+ verticalOffset + " Horizontal Offset(" + width + "): "
				+ horizontalOffset);

		if (spriteSheetImage == null) {
			logger.error("Could not load sprite because the sprite sheet image was null");
			// throw new
			// MissingSpriteSheetException("Image was null. TODO - Lookup name");
		}

		BufferedImage image = spriteSheetImage.getSubimage((horizontalOffset)
				* width, verticalOffset * height, width, height);
		return image;
	}

	/***
	 * Returns a subsection of an Image.
	 * 
	 * @param spriteSheetImage
	 * @param ref
	 * @param width
	 * @param height
	 * @param horizontalOffset
	 * @param verticalOffset
	 * @return
	 */
	public static Image getSubImage(Image spriteSheetImage, String ref,
			int width, int height, int horizontalOffset, int verticalOffset) {
		BufferedImage image = Image.getSubImage(
				spriteSheetImage.getBufferedImage(), width, height,
				horizontalOffset, verticalOffset);

		return wrapImage(image, ref, image.getWidth(), image.getHeight(),
				horizontalOffset, verticalOffset);
	}

	/***
	 * Loads an image from a file path and wraps it in an Image object, which
	 * means it will have an OpenGLTexture
	 * 
	 * @param ref
	 * @return
	 */
	public static Image loadImageFromFile(String ref) {
		BufferedImage image = loadImage(ref);
		logger.info("Sprite: " + ref + " is loading..");
		return wrapImage(image, ref, image.getWidth(), image.getHeight(), 0, 0);
	}

	/***
	 * Loads an image from a file path
	 * 
	 * @param ref
	 * @return
	 */
	private static BufferedImage loadImage(String ref) {
		BufferedImage image = null;

		try {
			InputStream imageStream = Animation.class.getClassLoader()
					.getResourceAsStream("images/" + ref);
			if (imageStream == null)
				throw new MissingSpriteSheetException("foo");

			image = ImageIO.read(imageStream);
		} catch (Exception e) {
			logger.error("Failed to load spritesheet: " + ref + " Exception: "
					+ e);
			return null;
		}

		return image;
	}

	/***
	 * Creates an Image object containing image information, a BufferedImage,
	 * and an OpenGLTexture
	 * 
	 * @param image
	 * @param ref
	 * @param width
	 * @param height
	 * @param horizontalOffset
	 * @param verticalOffset
	 * @return
	 */
	private static Image wrapImage(BufferedImage image, String ref, int width,
			int height, int horizontalOffset, int verticalOffset) {
		Image resultImage = new Image();
		resultImage.setRef(ref);

		logger.info("Sprite: " + ref + " is loading..");

		resultImage.setHorizontalOffset(horizontalOffset);
		resultImage.setVerticalOffset(verticalOffset);
		resultImage.setWidth(image.getWidth());
		resultImage.setHeight(image.getHeight());
		resultImage.setBufferedImage(image);
		resultImage.setTexture(createOpenGLTexture(image));

		return resultImage;
	}

	/***
	 * Responsible for creating an OpenGL Texture for a BufferedImage
	 * 
	 * @param image
	 * @return
	 */
	private static Texture createOpenGLTexture(BufferedImage image) {
		int srcPixelFormat;

		int textureID = createTextureID();
		Texture texture = new Texture(GL_TEXTURE_2D, textureID);

		// bind this texture
		glBindTexture(GL_TEXTURE_2D, textureID);

		texture.setWidth(image.getWidth());
		texture.setHeight(image.getHeight());

		if (image.getColorModel().hasAlpha()) {
			srcPixelFormat = GL_RGBA;
		} else {
			srcPixelFormat = GL_RGB;
		}

		WritableRaster raster;
		BufferedImage texImage;

		int texWidth = 2;
		int texHeight = 2;

		// find the closest power of 2 for the width and height
		// of the produced texture
		while (texWidth < image.getWidth()) {
			texWidth *= 2;
		}
		while (texHeight < image.getHeight()) {
			texHeight *= 2;
		}

		texture.setTextureHeight(texHeight);
		texture.setTextureWidth(texWidth);

		if (image.getColorModel().hasAlpha()) {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
					texWidth, texHeight, 4, null);
			texImage = new BufferedImage(glAlphaColorModel, raster, false,
					new Hashtable());
		} else {
			raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE,
					texWidth, texHeight, 3, null);
			texImage = new BufferedImage(glColorModel, raster, false,
					new Hashtable());
		}

		// copy the source image into the produced image
		Graphics g = texImage.getGraphics();
		g.setColor(new Color(0f, 0f, 0f, 0f));
		g.fillRect(0, 0, texWidth, texHeight);
		g.drawImage(image, 0, 0, null);

		ByteBuffer textureBuffer = null;

		byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer())
				.getData();

		textureBuffer = ByteBuffer.allocateDirect(data.length);
		textureBuffer.order(ByteOrder.nativeOrder());
		textureBuffer.put(data, 0, data.length);
		textureBuffer.flip();

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

		// produce a texture from the byte buffer
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, get2Fold(image.getWidth()),
				get2Fold(image.getHeight()), 0, srcPixelFormat,
				GL_UNSIGNED_BYTE, textureBuffer);

		return texture;
	}

	private static int get2Fold(int fold) {
		int ret = 2;
		while (ret < fold) {
			ret *= 2;
		}
		return ret;
	}

	private static int createTextureID() {
		glGenTextures(textureIDBuffer);
		return textureIDBuffer.get(0);
	}

	@Override
	public void draw(int x, int y) {
		// TODO Auto-generated method stub

		// store the current model matrix
		glPushMatrix();
		// bind to the appropriate texture for this sprite

		float width = texture.getImageWidth() * scale;
		float height = texture.getImageHeight() * scale;

		glHint(GL13.GL_TEXTURE_COMPRESSION_HINT, GL_NICEST);

		texture.bind();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// translate to the right location and prepare to draw

		glRotatef(this.rotation, 0f, 0f, 1f);
		glTranslatef(x, y, 0);

		// draw a quad textured to match the sprite
		glBegin(GL_QUADS);
		{
			glTexCoord2f(0, 0);
			glVertex2f(0, 0);

			glTexCoord2f(0, texture.getHeight());
			glVertex2f(0, height);

			glTexCoord2f(texture.getWidth(), texture.getHeight());
			glVertex2f(width, height);

			glTexCoord2f(texture.getWidth(), 0);
			glVertex2f(width, 0);
		}
		glEnd();

		glDisable(GL_BLEND);
		// restore the model view matrix to prevent contamination
		glPopMatrix();
		if (Window.isDebug()) {
			drawBoundingBox(x, y);
		}

	}

	protected void drawBoundingBox(int x, int y) {
		glDisable(GL_TEXTURE_2D);
		// glEnable(GL_COLOR_MATERIAL);

		glBegin(GL_LINES);
		glColor4f(0.f, 0.f, 1f, 1f);
		glVertex2f(x, y);
		glVertex2f(x + width * scale, y);

		glVertex2f(x + width * scale, y);
		glVertex2f(x + width * scale, y + height * scale);

		glVertex2f(x + width * scale, y + height * scale);
		glVertex2f(x, y + height * scale);

		glVertex2f(x, y + height * scale);
		glVertex2f(x, y);

		glEnd();
		glColor4f(1.f, 1.f, 1.f, 1.f);
		glEnable(GL_TEXTURE_2D);

	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	/***
	 * Loads an image from the file path specified on the Image object this
	 * function is called upon
	 * 
	 * @return
	 */
	public BufferedImage loadBufferedImageFromFile() {

		BufferedImage result = null;
		try {
			result = ImageIO.read(Animation.class.getClassLoader()
					.getSystemResourceAsStream("images/" + ref));
			result = result.getSubimage((horizontalOffset) * width,
					verticalOffset * height, width, height);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int degrees) {
		this.rotation = degrees;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public int getHorizontalOffset() {
		return horizontalOffset;
	}

	public void setHorizontalOffset(int horizontalOffset) {
		this.horizontalOffset = horizontalOffset;
	}

	public int getVerticalOffset() {
		return verticalOffset;
	}

	public void setVerticalOffset(int verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	public void setBufferedImage(BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
	}
}
