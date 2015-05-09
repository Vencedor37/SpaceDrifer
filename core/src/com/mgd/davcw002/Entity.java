package com.mgd.davcw002;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

/**
 * Created by Campbell on 7/05/2015.
 */
public class Entity {
    private Sprite baseSprite;
    private Body body;

    public void setBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
    }

    public void setBaseSprite(Sprite sprite) {
        baseSprite = sprite;
    }

    public Sprite getBaseSprite() {
        return baseSprite;
    }

    public void setLocation(float x, float y) {
        baseSprite.x = x;
        baseSprite.y = y;
    }

    public void setSize(int width, int height, int padding) {
        baseSprite.width = width;
        baseSprite.height = height;
        PolygonShape entityShape = new PolygonShape();
        entityShape.setAsBox(width/2 - padding, height/2 - padding);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = entityShape;
        fixtureDef.restitution = 2;
        body.createFixture(fixtureDef);
    }

    public void render(SpriteBatch batch) {
        baseSprite.render(batch);
    }

    public float getRelativeCentreX() {
        return baseSprite.width/2;
    }

    public float getRelativeCentreY() {
        return baseSprite.height/2;
    }

    public float getCentreX() {
        return (getRight() + getLeft())/2;
    }

    public float getCentreY() {
        return (getBottom() + getTop())/2;
    }

    public float getLeft() {
        return baseSprite.x;
    }

    public float getRight() {
        return baseSprite.x + baseSprite.width;
    }

    public float getTop() {
        return baseSprite.y;
    }

    public float getBottom() {
        return baseSprite.y + baseSprite.height;
    }

    public Vector2 getRelativeCentre() {
        return new Vector2(getRelativeCentreX(), getRelativeCentreY());
    }

    public void updateLocation() {
        baseSprite.x = body.getPosition().x;
        baseSprite.y = body.getPosition().y;
        checkBoundaries();
    }

    public void addForce(Vector2 force) {
        body.applyForceToCenter(force, true);
    }

    public Body getBody() {
        return body;
    }

    public void checkBoundaries() {
        float left = getLeft();
        float bottom = getBottom();
        float right = getRight();
        float top = getTop();

        if (left < 0) {
            setLocation(1, top);
            reverseAxis(true);
        }

        if (right > Gdx.graphics.getWidth()) {
            setLocation(Gdx.graphics.getWidth() - baseSprite.width - 1, top);
            reverseAxis(true);
        }
        if (bottom > Gdx.graphics.getHeight()) {
            setLocation(left, Gdx.graphics.getHeight() - 1 - baseSprite.height);
            reverseAxis(false);
        }
        if (top < 0) {
            setLocation(left, 1);
            reverseAxis(false);
        }
    }

    public void reverseAxis(boolean xAxis) {
        Vector2 currentVelocity = body.getLinearVelocity();
        if (xAxis) {
            body.setLinearVelocity(currentVelocity.x * -1, currentVelocity.y);
        } else {
            body.setLinearVelocity(currentVelocity.x, currentVelocity.y * -1);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entity entity = (Entity) o;

        if (!baseSprite.equals(entity.baseSprite)) return false;
        return body.equals(entity.body);

    }

    @Override
    public int hashCode() {
        int result = baseSprite.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }

    public void checkCollisions(List<Entity> otherEntities) {
        for (Entity entity : otherEntities) {
            if (!entity.equals(this)) {
                boolean onRight = this.getLeft() > entity.getRight();
                boolean above = this.getBottom() < entity.getTop();
                boolean onLeft = this.getRight() < entity.getLeft();
                boolean below = this.getTop() > entity.getBottom();
                if (!onRight && !onLeft) {
                    reverseAxis(true);
                }
                if (!above && !below) {
                    reverseAxis(false);
                }
            }
        }
    }

}
