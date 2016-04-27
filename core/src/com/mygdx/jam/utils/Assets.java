package com.mygdx.jam.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class Assets extends AssetManager {

    public static class Atlases {
//        public static final String Game = "atlases/gfx_game.atlas";

        public static class GameRegions {
//            public static final String Blessing = "blessing";
        }

    }

    public static class Sounds {
        public static final String Coin = "sounds/coin2.wav";
        public static final String FlameThrow = "sounds/flame_thrower.mp3";


    }

    public Assets() {
        Texture.setAssetManager(this);

        enqueueAssets();
    }

    public void enqueueAssets() {
        // Textures atlases
        load(Sounds.Coin, Sound.class);
        load(Sounds.FlameThrow, Sound.class);

    }

}
