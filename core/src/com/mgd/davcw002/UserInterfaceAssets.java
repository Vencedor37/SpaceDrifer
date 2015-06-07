package com.mgd.davcw002;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by campbelldavis on 7/06/15.
 */
public class UserInterfaceAssets {
  public SpriteDrawable startButtonUp;
  public SpriteDrawable startButtonDown;
  public SpriteDrawable startButtonChecked;

  public UserInterfaceAssets(float screenWidth, float screenHeight) {
    startButtonUp = new SpriteDrawable(new Sprite(new Texture("sprites/button_up.png")));
    startButtonDown = new SpriteDrawable(new Sprite(new Texture("sprites/button_down.png")));
    startButtonChecked = new SpriteDrawable(new Sprite(new Texture("sprites/button_down_checked.png")));
  }
}
