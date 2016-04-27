package com.mygdx.jam.model;

import com.badlogic.gdx.math.MathUtils;
import com.mygdx.jam.G;
import com.mygdx.jam.entities.Enemy;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-04-27.
 */
public class EnemyManager {

    private GameWorld gameWorld;

    private float timeToSpawn = 5;
    private float timeToSpawnDelta = -0.4f;
    private float timeLeftToSpawn = timeToSpawn;

    private float time;

    public EnemyManager(GameWorld gameWorld) {
        this.gameWorld = gameWorld;

        for (int i = -2; i < 3; i++) {
            Enemy enemy = new Enemy(G.TARGET_WIDTH / 2f - i * 200, G.TARGET_HEIGHT + 100, 24, gameWorld);
            gameWorld.getEntityManager().addEntity(enemy);
        }
    }

    public void update(float delta) {
        time += delta;
        timeLeftToSpawn -= delta;
        if(timeLeftToSpawn < 0) {

            for(int i = 0; i <= Math.max(1, (int)(time / 10f)); i++) {
                Enemy enemy = new Enemy(G.TARGET_WIDTH * MathUtils.random(0.1f, 0.9f), G.TARGET_HEIGHT + 100, 24, gameWorld);
                gameWorld.getEntityManager().addEntity(enemy);
            }

            timeToSpawn -= timeToSpawnDelta + MathUtils.random(-2f, 1f);
            timeLeftToSpawn = Math.max(0.2f, timeToSpawn);

        }

    }


}
