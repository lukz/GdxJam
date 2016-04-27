package com.mygdx.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Pool;
import com.mygdx.jam.model.Box2DWorld;
import com.mygdx.jam.model.GameWorld;
import com.mygdx.jam.model.PhysicsObject;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-29.
 */
public class Bullet extends Entity implements PhysicsObject, Pool.Poolable {
    public static final Pool<Bullet> pool = new Pool<Bullet>() {
        @Override protected Bullet newObject () {
            return new Bullet();
        }
    };
    // Physics
    private Body body;
    private boolean flagForDelete = false;

    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();
    private float SPEED = 5;

    private Sprite sprite;

    private float alive;
    private boolean active;
    private GameWorld gameWorld;

    public Bullet () {
        super(-1000, 0, 32, 32);


    }

    public void init(float x, float y, float vx, float vy, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        if (body == null) {
            this.body = gameWorld.getBox2DWorld().getBodyBuilder().fixture(
               gameWorld.getBox2DWorld().getFixtureDefBuilder().circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX).density(1f).friction(0.2f).restitution(0.5f)
//                                .maskBits(Box2DWorld.WALKER_MASK)
//                        .categoryBits(Box2DWorld.CATEGORY.ENEMY)
                  .build())
//                .fixedRotation()
               .angularDamping(10f).linearDamping(5f).position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX).type(BodyDef.BodyType.DynamicBody).userData(this).build();


            sprite = new Sprite(new Texture(Gdx.files.internal("coin.png")));
            gameWorld.getEntityManager().addEntity(this);
        }
        body.setActive(true);
        body.setTransform(x, y, 0);
        body.setLinearVelocity(vx, vy);
        alive = 0;
        active = true;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (!active) return;
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        if (!active) return;
        position.set(body.getPosition()).scl(Box2DWorld.BOX_TO_WORLD);

        velocity.set(direction).nor().scl(SPEED);
        alive += delta;

        if (alive > .5f) {
            pool.free(this);
        }
    }

    @Override public void drawDebug (ShapeRenderer shapeRenderer) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void handleBeginContact(PhysicsObject psycho2, GameWorld world) {

    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public boolean getFlagForDelete() {
        return flagForDelete;
    }

    @Override
    public void setFlagForDelete(boolean flag) {
        flagForDelete = flag;
    }

    public Vector2 getDirection() {
        return direction;
    }

    @Override public void reset () {
        body.setActive(false);
        body.setLinearVelocity(0, 0);
        body.setTransform(-1000, -1000, 0);
        alive = 0;
        active = false;
//        gameWorld.getEntityManager().removeEntity(this);
    }
}
