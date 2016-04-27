package com.mygdx.jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.mygdx.jam.screens.SplashScreen;
import com.mygdx.jam.utils.Assets;

public class Jam extends Game {

	private FPSLogger log;

	@Override
	public void create () {
		G.game = this;
		G.assets = new Assets();
		G.assets.load("fire.p", ParticleEffect.class);
		G.assets.load("fire2.p", ParticleEffect.class);
		G.assets.load("fire3.p", ParticleEffect.class);
		G.assets.load("coins.p", ParticleEffect.class);
		G.assets.load("blood.p", ParticleEffect.class);
		G.assets.load("coin.png", Texture.class);
		G.assets.load("sheep.png", Texture.class);
		G.assets.finishLoading();
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


