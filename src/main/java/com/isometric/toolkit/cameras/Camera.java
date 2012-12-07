package com.isometric.toolkit.cameras;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import com.isometric.toolkit.actoractions.Actionable;
import com.isometric.toolkit.cameraactions.CameraActionQueue;
import com.isometric.toolkit.cameraactions.CameraActionable;
import com.isometric.toolkit.engine.Vector;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.engine.Window;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.exceptions.RepeatForeverException;

/***
 * Camera abstraction to handle camera like operations in the engine.
 * 
 * @author Erik
 * 
 */
// TODO: Consider refactoring unessisary paramters, and clean up method names.
public class Camera {
	private Actor actorToFollow = null;
	private float distanceFromCenter = 0.0f;
	private Point pos = new Point(0.f, 0.f);
	private Vector shift = new Vector(0.f, 0.f);
	private Vector trans = new Vector(0.f, 0.f);
	private CameraActionQueue cameraActionQueue;

	private float width = 800;
	private float height = 600;
	private float scale = 1.0f;

	protected float dx;
	protected float dy;

	private float rotate = 0f;

	public Camera(Actor a, float distanceFromCenter, float width, float height) {
		actorToFollow = a;
		this.distanceFromCenter = distanceFromCenter;
		this.width = width;
		this.height = height;

		this.shift.setX(width / 2.f + a.getWidth() / 2.f);
		this.shift.setY(height / 2.f - a.getHeight() / 2.f);
		cameraActionQueue = new CameraActionQueue(this);

	}

	public Vector getShift() {
		return shift;
	}

	public void applyTransform() {
		glTranslatef(shift.getX(), shift.getY(), 0);
		glScalef(scale, scale, 0);
		glRotatef(rotate, 0, 0, 1.0f);
		glTranslatef(-shift.getX(), -shift.getY(), 0);

		glTranslatef(trans.getX() - pos.getX() + shift.getX(), trans.getY()
				- pos.getY() + shift.getY(), 0);

	}

	public void update(float delta) {

		// System.out.println("Distance: " +
		// pos.distance(actorToFollow.getPos()) + " Threshold: "+
		// distanceFromCenter + " Camera Pos: ("+pos.getX()+","+pos.getY()+")");
		float distance = 0.0f;
		if ((distance = pos.manhattanDistance(actorToFollow.getPos())) > distanceFromCenter) {
			Vector m = pos.sub(actorToFollow.getPos());
			m.normalize();
			m.scale(distance - distanceFromCenter);

			// m.scale(distance);
			pos.sub(m);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ADD)
				|| Keyboard.isKeyDown(Keyboard.KEY_EQUALS)) {

			scale += .1;
			Window.writeToDebug("Scale: " + scale);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)
				|| Keyboard.isKeyDown(Keyboard.KEY_MINUS)) {
			scale -= .1;
			Window.writeToDebug("Scale: " + scale);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			rotate += 1f;
			Window.writeToDebug("Rotate: " + rotate);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_L)) {
			rotate -= 1f;
			Window.writeToDebug("Rotate: " + rotate);
		}

		cameraActionQueue.update(delta);
	}

	public void addAction(CameraActionable a) {
		try {
			cameraActionQueue.add(a);
		} catch (RepeatForeverException e) {
			//logger.error(e.getMessage());
		}
	}

	public void removeAction(CameraActionable a) {
		cameraActionQueue.remove(a);
	}

	public static String getType() {
		return Actor.class.toString();
	}

	public Point getPos() {
		return pos;
	}

	public float getDistanceFromCenter() {
		return distanceFromCenter;
	}

	public void setDistanceFromCenter(float distanceFromCenter) {
		this.distanceFromCenter = distanceFromCenter;
	}

	public Actor getActorToFollow() {
		return actorToFollow;
	}

	public void setActorToFollow(Actor actorToFollow) {
		this.actorToFollow = actorToFollow;
	}

	public float getX() {
		return pos.getX();
	}

	public void setX(float x) {
		this.pos.setX(x);
	}

	public float getY() {
		return pos.getY();
	}

	public void setY(float y) {
		this.pos.setY(y);
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getScale() {
		return this.scale;
	}

	public void move(Vector delta) {
		this.trans.setX(this.trans.getX() + delta.getX());
		this.trans.setY(this.trans.getY() + delta.getY());
		this.dx = delta.getX();
		this.dy = delta.getY();
	}

}
