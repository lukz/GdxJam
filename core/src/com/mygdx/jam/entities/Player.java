package com.mygdx.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.jam.G;
import com.mygdx.jam.model.Box2DWorld;
import com.mygdx.jam.model.GameWorld;
import com.mygdx.jam.model.PhysicsObject;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-29.
 */
public class Player extends Entity implements PhysicsObject {

    private final ParticleEffect fireEffect;
    // Physics
    private Body body;
    private boolean flagForDelete = false;

    private Vector2 direction = new Vector2();
    private Vector2 velocity = new Vector2();
    private float SPEED = 5;

    private Sprite sprite;
    private Sprite healthSprite;
    public int fire;
    private GameWorld gameWorld;

    float hp = 1;
    private Color hpColor = new Color();

    public Player(float x, float y, float radius, GameWorld gameWorld) {
        super(x, y, radius * 2, radius * 2);
        this.gameWorld = gameWorld;

        this.body = gameWorld.getBox2DWorld().getBodyBuilder()
                .fixture(gameWorld.getBox2DWorld().getFixtureDefBuilder()
                        .circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(1f)
                        .friction(0.1f)
                        .restitution(0.5f)
//                                .maskBits(Box2DWorld.WALKER_MASK)
//                        .categoryBits(Box2DWorld.CATEGORY.ENEMY)
                        .build())
                .fixedRotation()
                .position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX)
                .type(BodyDef.BodyType.DynamicBody)
                .userData(this)
                .build();

        sprite = new Sprite(new Texture(Gdx.files.internal("dragon.png")));
        healthSprite = new Sprite(new Texture(Gdx.files.internal("pixel.png")));

        fireEffect = new ParticleEffect(G.assets.get("fire3.p", ParticleEffect.class));
        fireEffect.setPosition(x, y);
        fireEffect.reset();
    }

    @Override
    public void draw(SpriteBatch batch) {
        fireEffect.draw(batch);
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        sprite.draw(batch);

        // Draw health
        healthSprite.setBounds(0, 0, sprite.getWidth() * hp, 10);
        hpColor.set(Color.RED);
        hpColor.lerp(Color.GREEN, hp);
        hpColor.a = 0.9f;
        healthSprite.setColor(hpColor);
        healthSprite.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight() * 0.8f);
        healthSprite.draw(batch);
    }

    float fireDelay;
    @Override
    public void update(float delta) {
        Vector2 pos = body.getPosition();
        position.set(pos).scl(Box2DWorld.BOX_TO_WORLD);
        velocity.set(direction).nor().scl(SPEED);

        fireEffect.setPosition(position.x, position.y + .75f * Box2DWorld.BOX_TO_WORLD );
        fireEffect.update(delta);

        body.setLinearVelocity(velocity.x, velocity.y);
        fireDelay-= delta;
        if (fire > 0 && fireDelay <= 0) {
            fireEffect.start();
            fireDelay = 0.1f;
            Bullet bullet = new Bullet();
            bullet.setBounds(48, 48);
            bullet.type = Bullet.BulletType.PLAYER;
//            bullet.tint.set(Color.ORANGE);
            bullet.tint.set(0, 0, 0, 0);
            bullet.damage = 10f;
            bullet.init(pos.x, pos.y + .75f, 0, 5, gameWorld);
            bullet.body.setLinearDamping(.25f);
            bullet.alive = .8f;
        } else if (fire <= 0){
            fireEffect.allowCompletion();
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
