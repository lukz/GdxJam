package com.mygdx.jam.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.jam.G;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-04-27.
 */
public class BackgroundManager {

    private TextureRegion region;

    public BackgroundManager() {
        Texture bgTex = new Texture(Gdx.files.internal("bg1.png"));
        bgTex.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        region = new TextureRegion(bgTex);
        region.setRegionWidth(G.TARGET_WIDTH);
        region.setRegionHeight(G.TARGET_HEIGHT);
    }

    public void update(float delta) {
        region.scroll(0, -delta / 10);
    }

    public void draw(SpriteBatch batch) {
       batch.draw(region, 0, 0);
    }
}
