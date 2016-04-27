package com.mygdx.jam.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.mygdx.jam.entities.Player;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-04-27.
 */
public class PlayerController extends InputAdapter {

    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                player.getDirection().x = -1;
                return true;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                player.getDirection().x = 1;
                return true;
            case Input.Keys.UP:
            case Input.Keys.W:
                player.getDirection().y = 1;
                return true;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                player.getDirection().y = -1;
                return true;
            case Input.Keys.SPACE:
                player.fire = true;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
            case Input.Keys.A:
                if(player.getDirection().x == -1) player.getDirection().x = 0;
                return true;
            case Input.Keys.RIGHT:
            case Input.Keys.D:
                if(player.getDirection().x == 1) player.getDirection().x = 0;
                return true;
            case Input.Keys.UP:
            case Input.Keys.W:
                if(player.getDirection().y == 1) player.getDirection().y = 0;
                return true;
            case Input.Keys.DOWN:
            case Input.Keys.S:
                if(player.getDirection().y == -1) player.getDirection().y = 0;
                return true;
            case Input.Keys.SPACE:
                player.fire = false;
                return true;
        }
        return false;
    }
}
