package com.mygdx.jam.entities;

import com.badlogic.gdx.math.MathUtils;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-04-27.
 */
public class EnemyController {

    private Enemy enemy;
    private float time = MathUtils.random(10f);

    public EnemyController(Enemy enemy) {
        this.enemy = enemy;
    }

    public void update(float delta) {
        time+= delta;

        enemy.getDirection().x = (float)Math.sin(time * 2) / 4;
        enemy.getDirection().y = (float)Math.cos(time) / 3;
    }
}
