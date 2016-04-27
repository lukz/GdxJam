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
public class PlayerDead extends Entity {

    private final ParticleEffect fireEffect;
    private Sprite sprite;
    private GameWorld gameWorld;

    public PlayerDead (float x, float y, float radius, GameWorld gameWorld) {
        super(x, y, radius * 2, radius * 2);
        this.gameWorld = gameWorld;

        sprite = new Sprite(new Texture(Gdx.files.internal("dragon-dead.png")));

        fireEffect = new ParticleEffect(G.assets.get("fire3.p", ParticleEffect.class));
        fireEffect.setPosition(x, y);
        fireEffect.reset();
    }

    @Override
    public void draw(SpriteBatch batch) {
        fireEffect.draw(batch);
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        fireEffect.update(delta);
    }

    @Override public void drawDebug (ShapeRenderer shapeRenderer) {

    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
