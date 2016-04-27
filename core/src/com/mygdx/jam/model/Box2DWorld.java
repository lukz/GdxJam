package com.mygdx.jam.model;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.jam.utils.BodyBuilder;
import com.mygdx.jam.utils.FixtureDefBuilder;

import java.util.Iterator;


public class Box2DWorld {

    /*
     * Statics for calculation pixel to box2d metrics and vice versa
     */
    public static final float WORLD_TO_BOX = 0.01f; // 100px = 1m
    public static final float BOX_TO_WORLD = 100f;


    /*
     * Masks and categories used to filter collisions
     */
    public final static short ENEMY_MASK = CATEGORY.PLAYER | CATEGORY.PLAYER_BULLET;
    public final static short ENEMY_BULLET_MASK = CATEGORY.PLAYER;
    public final static short PLAYER_BULLET_MASK = CATEGORY.ENEMY | CATEGORY.ENEMY_BULLET;
    public final static short COIN_MASK = CATEGORY.PLAYER | CATEGORY.COIN;

    public final static class CATEGORY {
        public final static short ENEMY = 0x0001;
        public final static short PLAYER = 0x0002;
        public final static short PLAYER_BULLET = 0x0004;
        public final static short ENEMY_BULLET = 0x0008;
        public final static short COIN = 0x0016;
    };

    private World world;

    private FixtureDefBuilder fixtureDefBuilder;
    private BodyBuilder bodyBuilder;

    private Box2DDebugRenderer debugRenderer;

    private Array<Body> bodies = new Array<Body>();

    public Box2DWorld(Vector2 gravity) {
        world = new World(gravity, true);
        debugRenderer = new Box2DDebugRenderer(true, true, false, true, false, true);

        bodyBuilder = new BodyBuilder(world);
        fixtureDefBuilder = new FixtureDefBuilder();
    }

    public void update(float dt) {
        // Those are quite high values because tree is complex object you need a lot of iterations or it will freak out
        // I typically use 5 velocity iterations and 3 position iterations in my other games
        world.step(1/60f, 10, 4);
        sweepDeadBodies();
    }

    /*
	 * Bodies should be removed after world step to prevent simulation crash
	 */
	public void sweepDeadBodies() {
		world.getBodies(bodies);
		for (Iterator<Body> iter = bodies.iterator(); iter.hasNext();) {
			Body body = iter.next();
			if (body != null && (body.getUserData() instanceof PhysicsObject)) {
                PhysicsObject data = (PhysicsObject) body.getUserData();
				if (data.getFlagForDelete()) {
					getWorld().destroyBody(body);
				}
			}
		}
	}

    /*
	 * Box2D debug renderer
     */
    public void debugRender(Camera cam) {
        debugRenderer.render(world, cam.combined.cpy().scl(BOX_TO_WORLD));
    }

    public World getWorld() {
        return world;
    }

    public BodyBuilder getBodyBuilder() {
        return bodyBuilder;
    }

    public FixtureDefBuilder getFixtureDefBuilder() {
        return fixtureDefBuilder;
    }

}
