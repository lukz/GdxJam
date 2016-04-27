package com.mygdx.jam.entities;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.jam.G;
import com.mygdx.jam.model.GameWorld;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-29.
 */
public class Effect extends Entity {
    private GameWorld gameWorld;
    private ParticleEffect effect;

    public Effect (float x, float y, String name, GameWorld gameWorld) {
        super(x, y, 32, 32);
        this.gameWorld = gameWorld;
        gameWorld.getEntityManager().addEntity(this);
        effect = new ParticleEffect(G.assets.get(name, ParticleEffect.class));
        effect.setPosition(x, y);
        effect.reset();
    }

    @Override
    public void draw(SpriteBatch batch) {
        effect.draw(batch);
    }

    @Override
    public void update(float delta) {
        effect.update(delta);
        if (effect.isComplete()) {
            gameWorld.getEntityManager().removeEntity(this);
        }
    }

    @Override public void drawDebug (ShapeRenderer shapeRenderer) {

    }

    @Override
    public void dispose() {
    }
}
