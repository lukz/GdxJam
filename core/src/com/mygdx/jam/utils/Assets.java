package com.mygdx.jam.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets extends AssetManager {

    public static class Atlases {
//        public static final String Game = "atlases/gfx_game.atlas";

        public static class GameRegions {
//            public static final String Blessing = "blessing";
        }

    }

    public Assets() {
        Texture.setAssetManager(this);

        enqueueAssets();
    }

    public void enqueueAssets() {
        // Textures atlases
//        load(Atlases.Game, TextureAtlas.class);

    }

}
