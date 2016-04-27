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

    private Sprite sprite;
    private GameWorld gameWorld;

    public PlayerDead (float x, float y, float radius, GameWorld gameWorld) {
        super(x, y, radius * 2, radius * 2);
        this.gameWorld = gameWorld;

        sprite = new Sprite(new Texture(Gdx.files.internal("dragon-dead.png")));
        sprite.setPosition(x - sprite.getWidth()/2, y - sprite.getHeight()/2);
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    float fade = 1;
    @Override
    public void update(float delta) {
        fade -= delta * 0.5f;
        if (fade >= 0) {
            sprite.setColor(1, 1, 1, fade);
        }
    }

    @Override public void drawDebug (ShapeRenderer shapeRenderer) {

    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
