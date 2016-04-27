package com.mygdx.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
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
public class Bullet extends Entity implements PhysicsObject {
    public ParticleEffect effect;

    public enum BulletType {PLAYER, ENEMY}
    // Physics
    public Body body;
    private boolean flagForDelete = false;

    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();
    private float SPEED = 5;

    private Sprite sprite;

    public float alive;
    private GameWorld gameWorld;
    public BulletType type = BulletType.PLAYER;
    public Color tint = new Color();
    public float damage = 0;

    public Bullet () {
        super(-1000, 0, 32, 32);


    }

    public void init(float x, float y, float vx, float vy, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        this.body = gameWorld.getBox2DWorld().getBodyBuilder().fixture(
           gameWorld.getBox2DWorld().getFixtureDefBuilder().circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX).density(1f).friction(0.2f).restitution(0.5f)
//                                .maskBits(Box2DWorld.WALKER_MASK)
//                        .categoryBits(Box2DWorld.CATEGORY.ENEMY)
              .build())
//                .fixedRotation()
           .angularDamping(10f).linearDamping(5f).position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX).type(BodyDef.BodyType.DynamicBody).userData(this).build();



        sprite = new Sprite(new Texture(Gdx.files.internal("coin.png")));
        gameWorld.getEntityManager().addEntity(this);
        body.setActive(true);
        body.setTransform(x, y, 0);
        body.setLinearVelocity(vx, vy);
        alive = .5f;
        sprite.setSize(bounds.width, bounds.height);

    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setColor(tint);
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        sprite.draw(batch);
        if (effect != null) {
            effect.draw(batch);
        }
    }

    @Override
    public void update(float delta) {
        position.set(body.getPosition()).scl(Box2DWorld.BOX_TO_WORLD);

        velocity.set(direction).nor().scl(SPEED);
        alive -= delta;

        if (effect != null) {
            effect.setPosition(position.x, position.y);
            effect.update(delta);
        }

        if (alive <= 0) {
            gameWorld.getEntityManager().removeEntity(this);
        }
    }

    @Override public void drawDebug (ShapeRenderer shapeRenderer) {

    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    @Override
    public void handleBeginContact(PhysicsObject psycho2, GameWorld world) {
        if (psycho2 instanceof Bullet) {
            Bullet other = (Bullet)psycho2;
            if (type == BulletType.PLAYER && other.type == BulletType.ENEMY) {
                gameWorld.getEntityManager().removeEntity(other);
            }
        } else if (psycho2 instanceof Player) {
            Player player = (Player)psycho2;
            if (type == BulletType.ENEMY) {
                player.hp -= damage;
            }
            gameWorld.getEntityManager().removeEntity(this);
        } else if (psycho2 instanceof Enemy) {
            Enemy enemy = (Enemy)psycho2;
            if (type == BulletType.PLAYER) {
                enemy.health -= damage;
                gameWorld.getEntityManager().removeEntity(this);
            }
        }
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

    public void setBounds (float width, float height) {
        bounds.setSize(width ,height);
    }
}
