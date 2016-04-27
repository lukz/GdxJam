package com.mygdx.jam.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.jam.G;
import com.mygdx.jam.Jam;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = G.TARGET_WIDTH;
		config.height = G.TARGET_HEIGHT;

		new LwjglApplication(new Jam(), config);
	}
}
