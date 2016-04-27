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
        public static final String Laser1 = "sounds/laser1.mp3";
        public static final String Laser2 = "sounds/laser2.mp3";
        public static final String Laser3 = "sounds/laser3.mp3";
        public static final String Laser4 = "sounds/laser4.mp3";
        public static final String Laser5 = "sounds/laser5.mp3";
        public static final String Laser6 = "sounds/laser6.mp3";
        public static final String Laser7 = "sounds/laser7.mp3";
        public static final String Laser8 = "sounds/laser8.mp3";
        public static final String Laser9 = "sounds/laser9.mp3";


    }

    public Assets() {
        Texture.setAssetManager(this);

        enqueueAssets();
    }

    public void enqueueAssets() {
        // Textures atlases
        load(Sounds.Coin, Sound.class);
        load(Sounds.FlameThrow, Sound.class);
        load(Sounds.Laser1, Sound.class);
        load(Sounds.Laser2, Sound.class);
        load(Sounds.Laser3, Sound.class);
        load(Sounds.Laser4, Sound.class);
        load(Sounds.Laser5, Sound.class);
        load(Sounds.Laser6, Sound.class);
        load(Sounds.Laser7, Sound.class);
        load(Sounds.Laser8, Sound.class);
        load(Sounds.Laser9, Sound.class);

    }

}
