package com.mygdx.jam.entities;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.mygdx.jam.G;
import com.mygdx.jam.model.GameWorld;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-29.
 */
public class CoinEffect extends Entity {
    private GameWorld gameWorld;
    private ParticleEffect effect;

    public CoinEffect () {
        super(0, 0, 32, 32);
    }

    public void init(float x, float y, GameWorld gameWorld) {
        this.gameWorld = gameWorld;
        gameWorld.getEntityManager().addEntity(this);
        effect = new ParticleEffect(G.assets.get("coins.p", ParticleEffect.class));
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
