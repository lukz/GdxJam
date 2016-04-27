package com.mygdx.jam.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.mygdx.jam.G;
import com.mygdx.jam.model.Box2DWorld;
import com.mygdx.jam.model.GameWorld;
import com.mygdx.jam.model.PhysicsObject;

/**
 * @author Lukasz Zmudziak, @lukz_dev on 2016-01-29.
 */
public class Coin extends Entity implements PhysicsObject {
    // Physics
    public Body body;
    private boolean flagForDelete = false;

    private float SPEED = 5;

    private Sprite sprite;

    private GameWorld gameWorld;

    public Coin (float x, float y, GameWorld gameWorld) {
        super(x, y, 32, 32);
        this.gameWorld = gameWorld;
        this.body = gameWorld.getBox2DWorld().getBodyBuilder().fixture(
           gameWorld.getBox2DWorld().getFixtureDefBuilder().circleShape(getBounds().getWidth() / 2 * Box2DWorld.WORLD_TO_BOX).density(1f).friction(0.2f).restitution(0.5f)
                                .maskBits(Box2DWorld.POWERUP_MASK)
                        .categoryBits(Box2DWorld.CATEGORY.POWERUP)
              .build())
//                .fixedRotation()
           .angularDamping(10f).linearDamping(5f).position(x * Box2DWorld.WORLD_TO_BOX, y * Box2DWorld.WORLD_TO_BOX).type(BodyDef.BodyType.DynamicBody).userData(this).build();



        sprite = new Sprite(G.assets.get("coin.png", Texture.class));
        gameWorld.getEntityManager().addEntity(this);
        body.setTransform(x, y, 0);
        sprite.setSize(bounds.width, bounds.height);
    }

    @Override
    public void draw(SpriteBatch batch) {
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
        sprite.draw(batch);
    }

    @Override
    public void update(float delta) {
        position.set(body.getPosition()).scl(Box2DWorld.BOX_TO_WORLD);
    }

    @Override public void drawDebug (ShapeRenderer shapeRenderer) {

    }

    @Override
    public void dispose() {
        sprite.getTexture().dispose();
    }

    @Override
    public void handleBeginContact(PhysicsObject psycho2, GameWorld world) {
        if (psycho2 instanceof Player) {
            Player player = (Player)psycho2;
            new Effect(position.x, position.y, "coins.p", gameWorld);
            gameWorld.getEntityManager().removeEntity(this);
            player.coins += 1;
        }
    }

    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public boolean getFlagForDelete() {
        return flagForDelete;
    }

    @Override
    public void setFlagForDelete(boolean flag) {
        flagForDelete = flag;
    }

    public void setBounds (float width, float height) {
        bounds.setSize(width ,height);
    }
}
