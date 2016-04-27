package com.mygdx.jam.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.jam.G;
import com.mygdx.jam.controllers.PlayerController;
import com.mygdx.jam.controllers.PlayerGamepadController;
import com.mygdx.jam.entities.Enemy;
import com.mygdx.jam.entities.Player;
import com.mygdx.jam.entities.Wall;
import com.mygdx.jam.entities.*;
import com.mygdx.jam.utils.Constants;

public class GameWorld implements ContactListener {

    private Box2DWorld box2DWorld;

    // Managers
    private EntityManager entityManager;
    private BackgroundManager backgroundManager;
    private EnemyManager enemyManager;
    public Player player;
    public Player player2;
    private PlayerDead dead;

    public static enum GameState { WAITING_TO_START, IN_GAME, FINISH };
    private GameState gameState = GameState.WAITING_TO_START;


    public int coins;

    public GameWorld () {

        box2DWorld = new Box2DWorld(new Vector2(0, Constants.GRAVITY));

        entityManager = new EntityManager();
        backgroundManager = new BackgroundManager();
        enemyManager = new EnemyManager(this);

        // Pass all collisions through this class
        box2DWorld.getWorld().setContactListener(this);

        initializeObjects();
    }


    public void initializeObjects() {
        // Player
        player = new Player(G.TARGET_WIDTH / 2f, 80, 40, this);
        entityManager.addEntity(player);
        Gdx.input.setInputProcessor(new PlayerController(player));

        if(Controllers.getControllers().size > 0) {
            Controllers.getControllers().get(0).addListener(new PlayerGamepadController(player));
        }

        if(Controllers.getControllers().size > 1) {
            player2 = new Player(G.TARGET_WIDTH / 2f, 80, 40, this);

            Color color = new Color(Color.BLUE);
            color.lerp(Color.WHITE, 0.5f);
            player2.getSprite().setColor(color);
            Controllers.getControllers().get(1).addListener(new PlayerGamepadController(player2));
            entityManager.addEntity(player2);
        }

        // Walls
        new Wall(0, G.TARGET_HEIGHT / 2, 20, G.TARGET_HEIGHT, this);
        new Wall(G.TARGET_WIDTH, G.TARGET_HEIGHT / 2, 20, G.TARGET_HEIGHT, this);
        new Wall(G.TARGET_WIDTH / 2, 0, G.TARGET_WIDTH, 20, this);
    }

    public void update(float delta) {
        GdxAI.getTimepiece().update(delta);

        // Update physics
        box2DWorld.update(delta);

        entityManager.update(delta);
        backgroundManager.update(delta);
        enemyManager.update(delta);

        if (player != null && player.hp <= 0) {
            Vector2 position = player.getPosition();
            entityManager.addEntity(dead = new PlayerDead(position.x, position.y, 40, this));
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            entityManager.removeEntity(player);
            player = null;
            Gdx.input.setInputProcessor(null);
        }

        if (player2 != null && player2.hp <= 0) {
            Vector2 position = player2.getPosition();
            entityManager.addEntity(dead = new PlayerDead(position.x, position.y, 40, this));
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            new Effect(position.x + MathUtils.random(-32, 32), position.y + MathUtils.random(-32, 32), "blood.p", this);
            entityManager.removeEntity(player2);
            player2 = null;
            Gdx.input.setInputProcessor(null);
        }



        if (player == null && player2 == null && Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            if (dead != null) entityManager.removeEntity(dead);
            player = new Player(G.TARGET_WIDTH / 2f, 80, 40, this);
            entityManager.addEntity(player);
            Gdx.input.setInputProcessor(new PlayerController(player));

            if(Controllers.getControllers().size > 0) {
                Controllers.getControllers().get(0).addListener(new PlayerGamepadController(player));
            }

            if(Controllers.getControllers().size > 1) {
                player2 = new Player(G.TARGET_WIDTH / 2f, 80, 40, this);
                entityManager.addEntity(player2);
                Controllers.getControllers().get(1).addListener(new PlayerGamepadController(player2));
            }

            for (Entity entity : entityManager.getEntitiesClass(Enemy.class)) {
                entityManager.removeEntity(entity);
            }

            for (Entity entity : entityManager.getEntitiesClass(Bullet.class)) {
                entityManager.removeEntity(entity);
            }

        }
    }


    public void draw(SpriteBatch batch) {
        backgroundManager.draw(batch);
        entityManager.draw(batch);
    }

    @Override
    public void beginContact(Contact contact) {
        Object ent1 = contact.getFixtureA().getBody().getUserData();
        Object ent2 = contact.getFixtureB().getBody().getUserData();

        if(!(ent1 instanceof PhysicsObject) || !(ent2 instanceof PhysicsObject)) {
            return;
        }

        PhysicsObject physo1 = (PhysicsObject)ent1;
        PhysicsObject physo2 = (PhysicsObject)ent2;

        physo1.handleBeginContact(physo2, this);
        physo2.handleBeginContact(physo1, this);
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Box2DWorld getBox2DWorld() {
        return box2DWorld;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void dispose() {
        entityManager.dispose();
    }

}
