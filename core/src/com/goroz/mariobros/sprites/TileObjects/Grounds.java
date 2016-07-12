package com.goroz.mariobros.sprites.TileObjects;

import com.badlogic.gdx.math.Rectangle;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.screen.PlayScreen;
import com.goroz.mariobros.sprites.Mario;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Grounds extends InteractiveTileObject {
    public Grounds(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoriesFilter(MarioBros.OBJECT_BIT);

    }

    @Override
    public void onHeadHit() {

    }
}
