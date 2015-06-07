package com.mgd.davcw002;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Created by Campbell on 16/05/2015.
 */
public class Helpers {
    private static GlyphLayout glyphLayout = null;
    public static float getTextWidth(BitmapFont font, CharSequence chars) {
        if (glyphLayout == null) {
            glyphLayout = new GlyphLayout();
        }
        glyphLayout.setText(font, chars);
        return glyphLayout.width;
    }

    public static float getTextHeight(BitmapFont font, CharSequence chars) {
        if (glyphLayout == null) {
            glyphLayout = new GlyphLayout();
        }
        glyphLayout.setText(font, chars);
        return glyphLayout.height;
    }
}
