package com.mygdx.jam.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.jam.G;
import com.mygdx.jam.model.Box2DWorld;
import com.mygdx.jam.model.GameWorld;
import com.mygdx.jam.model.PhysicsObject;
import com.mygdx.jam.utils.Assets;

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
    private Sprite spriteAttack;
    private Sprite healthSprite;
    public boolean fire;
    private GameWorld gameWorld;
    private boolean wasHit;

    public float hp = 1;
    private Color hpColor = new Color();

    private Sound flameThrow;
    private long flameThrowId;
    private float flameThrowVol = 0;

    public Player(float x, float y, float radius, GameWorld gameWorld) {
        super(x, y, radius * 2, radius * 2);
        this.gameWorld = gameWorld;

        flameThrow = G.assets.get(Assets.Sounds.FlameThrow, Sound.class);
        flameThrowId = flameThrow.play(0);
        flameThrow.setLooping(flameThrowId, true);

        this.body = gameWorld.getBox2DWorld().getBodyBuilder()
                .fixture(gameWorld.getBox2DWorld().getFixtureDefBuilder()
                        .circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX)
                        .density(1f)
                        .friction(0.1f)
                        .restitution(0.5f)
//                                .maskBits(Box2DWorld.MA)
                        .categoryBits(Box2DWorld.CATEGORY.PLAYER)
                        .build())
                .fixedRotation()
                .position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX)
                .type(BodyDef.BodyType.DynamicBody)
                .userData(this)
                .build();

        sprite = new Sprite(new Texture(Gdx.files.internal("dragon.png")));
        spriteAttack = new Sprite(new Texture(Gdx.files.internal("dragon-attack.png")));
        healthSprite = new Sprite(new Texture(Gdx.files.internal("pixel.png")));

        fireEffect = new ParticleEffect(G.assets.get("fire3.p", ParticleEffect.class));
        fireEffect.setPosition(x, y);
        fireEffect.reset();
    }

    @Override
    public void draw(SpriteBatch batch) {
        fireEffect.draw(batch);

        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        spriteAttack.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        if (wasHit) {
            batch.setShader(G.shader);
        }
        if (fire) {
            spriteAttack.setColor(sprite.getColor());
            spriteAttack.draw(batch);
        } else {
            sprite.draw(batch);
        }

        if (wasHit) {
            batch.setShader(null);
            wasHit = false;
        }

        // Draw health
        healthSprite.setBounds(0, 0, sprite.getWidth() * hp, 10);
        hpColor.set(Color.RED);
        hpColor.lerp(Color.GREEN, hp);
        hpColor.a = 0.9f;
        healthSprite.setColor(hpColor);
        healthSprite.setPosition(sprite.getX(), sprite.getY() + sprite.getHeight() * 0.1f);
        healthSprite.draw(batch);
    }

    float fireDelay;
    @Override
    public void update(float delta) {
        Vector2 pos = body.getPosition();
        position.set(pos).scl(Box2DWorld.BOX_TO_WORLD);
        velocity.set(direction).nor().scl(SPEED);
        if (direction.x > 0) {
            sprite.setFlip(false, false);
            spriteAttack.setFlip(false, false);
        } else if (direction.x < 0) {
            sprite.setFlip(true, false);
            spriteAttack.setFlip(true, false);
        }

        fireEffect.setPosition(position.x, position.y + .75f * Box2DWorld.BOX_TO_WORLD );
        fireEffect.update(delta);

        body.setLinearVelocity(velocity.x, velocity.y);
        fireDelay-= delta;


        if(fire) {
            flameThrowVol = Math.min(0.7f, flameThrowVol + delta);
        }


        if (fire && fireDelay <= 0) {
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
        } else if (!fire){
            fireEffect.allowCompletion();

            flameThrowVol = Math.max(0, flameThrowVol - delta);
        }

        flameThrow.setVolume(flameThrowId, flameThrowVol);
    }

    @Override public void drawDebug (ShapeRenderer shapeRenderer) {

    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
        flameThrow.stop(flameThrowId);
    }

    @Override
    public void handleBeginContact(PhysicsObject psycho2, GameWorld world) {

    }

    public void dmg(float value) {
        hp -= value;
        wasHit = true;

        if(hp <= 0) {
            flameThrow.stop(flameThrowId);
        }


        G.assets.get(Assets.Sounds.Hit, Sound.class).play(0.6f, 1 + MathUtils.random(-0.1f, 0.1f), 0);
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

    public Sprite getSprite() {
        return sprite;
    }
}
