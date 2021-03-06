package com.mygdx.jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.mygdx.jam.utils.Assets;

public class G {
    public static boolean DEBUG = false;

    // Virtual resolution - potato units
    public static int TARGET_WIDTH = 1280;
    public static int TARGET_HEIGHT = 720;

    // Game instance used to move between screens
    public static Game game;
    public static Assets assets;

    public static ShaderProgram shader;
}
