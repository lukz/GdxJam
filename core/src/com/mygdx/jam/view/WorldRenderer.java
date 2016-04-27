package com.mygdx.jam.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.jam.G;
import com.mygdx.jam.model.GameWorld;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2015-11-16.
 */
public class WorldRenderer {

    private final Texture texture;
    private GameWorld gameWorld;

    private OrthographicCamera cam;
    private Viewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private Array<Sprite> coins = new Array<Sprite>();

    public static float SHAKE_TIME = 0;

    private Color clearColor = Color.valueOf("817e79");

    public WorldRenderer(GameWorld world) {
        this.gameWorld = world;
        cam = new OrthographicCamera();

        // Let's show more game world when window resized
        viewport = new ExtendViewport(G.TARGET_WIDTH, G.TARGET_HEIGHT, cam);

        // Batch used for
        batch = new SpriteBatch();

        font = G.assets.get("fonts/universidad.fnt", BitmapFont.class);
        font.setUseIntegerPositions(false);
        texture = G.assets.get("coin.png", Texture.class);
    }

    boolean left;
    float restart;
    public void render(float delta) {
        if(SHAKE_TIME > 0) {
            SHAKE_TIME -= delta;
            cam.position.x = G.TARGET_WIDTH / 2 + MathUtils.random(-10, 10);
            cam.position.y = G.TARGET_HEIGHT / 2 + MathUtils.random(-10, 10);
            Gdx.input.vibrate((int)(SHAKE_TIME * 1000));
        } else {
            cam.position.x = G.TARGET_WIDTH / 2;
            cam.position.y = G.TARGET_HEIGHT / 2;
        }

        // Update camera
        cam.update();
        batch.setProjectionMatrix(cam.combined);

        // Clear screen
        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Render world
        gameWorld.draw(batch);

        batch.end();

        if (G.DEBUG) {
            gameWorld.getBox2DWorld().debugRender(cam);
        }

        int score = gameWorld.coins;
        batch.begin();
        font.draw(batch, Integer.toString(score), G.TARGET_WIDTH / 2, 75, 0, Align.center, false);
        if (coins.size > score) coins.clear();
        if (coins.size < score) {
            for (int i = 0; i < score - coins.size; i++) {
                Sprite sprite = new Sprite(texture);
                sprite.setScale(0.6f);
                sprite.setAlpha(0.6f);
                coins.add(sprite);
                float ox = MathUtils.random(-48, 48);
                float oy = MathUtils.random(-32, 32);
                if (left) {
                    sprite.setPosition(G.TARGET_WIDTH / 2 - 100 - sprite.getWidth()/2f + ox, 40 + oy);
                } else {
                    sprite.setPosition(G.TARGET_WIDTH / 2 + 100 - sprite.getWidth()/2f + ox, 40 + oy);
                }
                left = !left;
            }
        }
        for (Sprite sprite : coins) {
            sprite.draw(batch);
        }
        if (gameWorld.player == null && gameWorld.player2 == null) {
            restart += delta;
            font.setColor(1, 1, 1, restart);
            font.draw(batch, "<SPACE>", G.TARGET_WIDTH / 2, G.TARGET_HEIGHT / 2, 0, Align.center, false);
        }
        batch.end();
        batch.setColor(Color.WHITE);
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public void dispose() {
        batch.dispose();
    }
}
