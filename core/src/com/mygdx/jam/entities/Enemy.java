package com.mygdx.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.jam.G;
import com.mygdx.jam.model.Box2DWorld;
import com.mygdx.jam.model.GameWorld;
import com.mygdx.jam.model.PhysicsObject;
import com.mygdx.jam.utils.Assets;
import com.mygdx.jam.view.WorldRenderer;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-29.
 */
public class Enemy extends Entity implements PhysicsObject {


    // Physics
    private Body body;
    private boolean flagForDelete = false;

    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();
    private float SPEED = MathUtils.random(2f, 4f);

    private Sprite sprite;
    public float health = 10;
    private GameWorld gameWorld;

    private EnemyController enemyController = new EnemyController(this);

    private Vector2 targetPosition = new Vector2();
    private boolean targetPositionReached = false;

    private boolean isUfo;

    public Enemy (float x, float y, float radius, GameWorld gameWorld, boolean isUfo) {
        super(x, y, radius * 2, radius * 2);
        this.gameWorld = gameWorld;
        this.isUfo = isUfo;

        targetPosition.set(x, G.TARGET_HEIGHT * 0.6f);

        this.body = gameWorld.getBox2DWorld().getBodyBuilder()
                .fixture(gameWorld.getBox2DWorld().getFixtureDefBuilder()
                        .circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(1f)
                        .friction(0.1f)
                        .restitution(0.5f)
                        .maskBits(Box2DWorld.ENEMY_MASK)
                        .categoryBits(Box2DWorld.CATEGORY.ENEMY)
                        .build())
                .fixedRotation()
                .position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX)
                .type(BodyDef.BodyType.DynamicBody)
                .userData(this)
                .build();

        if(!isUfo) {
            sprite = new Sprite(new Texture(Gdx.files.internal("enemy/enemy ("+ MathUtils.random(1, 20)+").png")));
        } else {
            sprite = new Sprite(new Texture(Gdx.files.internal("enemy/ufo ("+ MathUtils.random(1, 4)+").png")));
        }
        sprite.setScale(0.7f);
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    float fireDelay = MathUtils.random(1.5f);
    @Override
    public void update(float delta) {
        Vector2 pos = body.getPosition();
        position.set(pos).scl(Box2DWorld.BOX_TO_WORLD);


        if(!targetPositionReached) {
            position.lerp(targetPosition, delta * SPEED / 2);
            body.setTransform(position.x * Box2DWorld.WORLD_TO_BOX, position.y * Box2DWorld.WORLD_TO_BOX, 0);

            if(position.dst(targetPosition) < 40) {
                targetPositionReached = true;
            }
        } else {
            enemyController.update(delta);

        }

        velocity.set(direction).scl(SPEED);


        body.setLinearVelocity(velocity.x, velocity.y);
        if (health <= 0 ) {

            // FFS
            new Coin(position.x * Box2DWorld.WORLD_TO_BOX, position.y * Box2DWorld.WORLD_TO_BOX, gameWorld);

            gameWorld.getEntityManager().removeEntity(this);
            new Effect(position.x, position.y, "alien-death.p", gameWorld);

            WorldRenderer.SHAKE_TIME += 0.2f;

            G.assets.get(Assets.Sounds.Explosion, Sound.class).play(0.4f, 1 + MathUtils.random(-0.1f, 0.1f), 0);

        } else {
            fireDelay -= delta;
            if (fireDelay <= 0) {

                if(!isUfo) {
                    fireDelay = 1.5f;
                    Bullet bullet = new Bullet();
                    bullet.setBounds(24, 24);
                    bullet.type = Bullet.BulletType.ENEMY;
                    bullet.tint.set(Color.GREEN);
                    bullet.damage = 0.1f;
                    bullet.init(pos.x, pos.y - .75f, 0, -16, gameWorld);
                } else {
                    Vector2 direction = new Vector2();

                    for(int i = 0; i < 8; i++) {

                        direction.set(0, -16);
                        direction.setAngle(360f / 8f * i);

                        Bullet bullet = new Bullet();
                        bullet.setBounds(24, 24);
                        bullet.type = Bullet.BulletType.ENEMY;
                        bullet.tint.set(Color.GREEN);
                        bullet.damage = 0.1f;
                        bullet.init(pos.x, pos.y - .75f, direction.x, direction.y, gameWorld);
                    }

                    fireDelay = 3f;

                }

                G.assets.get("sounds/laser"+MathUtils.random(1,9)+".mp3", Sound.class).play(0.5f);

            }


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
        if (psycho2 instanceof Player) {
            Player player = (Player)psycho2;
            player.dmg(0.25f);
            WorldRenderer.SHAKE_TIME += 0.1f;
            health -= 20;
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
}
