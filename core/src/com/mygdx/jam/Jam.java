package com.mygdx.jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.mygdx.jam.screens.SplashScreen;
import com.mygdx.jam.utils.Assets;

public class Jam extends Game {

	private FPSLogger log;

	@Override
	public void create () {
		G.game = this;
		G.assets = new Assets();

		log = new FPSLogger();

		G.game.setScreen(new SplashScreen());
	}

	@Override
	public void render() {
		super.render();
		log.log();
	}

	@Override public void dispose () {
		super.dispose();
		G.assets.dispose();
	}
}


