package com.mgd.davcw002;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Campbell on 7/05/2015.
 */
public class Sprite {
    private Texture texture;
    public float x = 0;
    public float y = 0;

    public float width = 10;
    public float height = 10;

    public Sprite(String imgPath) {
        texture = new Texture(imgPath);
    }

    public void render(SpriteBatch batch) {
        batch.begin();
        batch.draw(texture, x, y, width, height);
        batch.end();
    }
}
