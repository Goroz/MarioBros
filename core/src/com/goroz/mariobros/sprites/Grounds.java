package com.goroz.mariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.screen.PlayScreen;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Grounds extends InteractiveTileObject {
    public Grounds(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);

    }

    @Override
    public void onHeadHit() {

    }

    @Override
    public void onFootHit() {
        Mario.jumpCount = 0;
    }
}
