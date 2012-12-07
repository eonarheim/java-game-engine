package com.isometric.toolkit.cameraactions;

import org.newdawn.slick.util.Log;

import com.isometric.toolkit.engine.Vector;
import com.isometric.toolkit.engine.Point;
import com.isometric.toolkit.entities.Actor;
import com.isometric.toolkit.cameras.*;

/***
 * Action to move an Camera to a destination at a certain speed.
 * 
 * @author Jeff
 * 
 */
public class CameraFollowActor implements CameraActionable {
	private Point start;
	// private Point end;
	private Vector delta;
	private float distanceFromCenter;
	private boolean hasStarted = false;
	private Point pos = new Point(0.f, 0.f);
	private Actor actorToFollow;
	float distance;
	float speed;
	Camera camera;

	/***
	 * Action to move an camera to a destination at a certain speed.
	 * 
	 * @param camera
	 * @param destination
	 * @param speed
	 */
	public CameraFollowActor(Camera camera, Actor actorToFollow,
			float distanceFromCenter) {
		// Store internal camera

		this.camera = camera;
		this.actorToFollow = actorToFollow;
		this.distanceFromCenter = distanceFromCenter;
	}

	@Override
	public Point getPos() {
		return camera.getPos();
	}

	@Override
	public void update(float delta) {
		if (!hasStarted()) {
			setStarted(true);
		}

		float distance = 0.0f;	
		if ((distance = pos.manhattanDistance(actorToFollow.getPos())) > distanceFromCenter) {
			Vector m = pos.sub(actorToFollow.getPos());
			m.normalize();
			m.scale(distance - distanceFromCenter);
			
			camera.move(m);
			pos.sub(m);
		}  
	}

	@Override
	public boolean isComplete(Camera a) {
		return false;
		// return a.getPos().distance(start) >= distance;
	}

	@Override
	public boolean hasStarted() {
		return this.hasStarted;
	}

	private void setStarted(boolean hasStarted) {
		this.hasStarted = hasStarted;
	}

	@Override
	public void reset(Camera a) {
		hasStarted = false;

	}

}
