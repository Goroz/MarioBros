package com.goroz.mariobros.sprites.TileObjects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.screen.PlayScreen;
import com.goroz.mariobros.sprites.Mario;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Grounds extends InteractiveTileObject {
    public Grounds(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoriesFilter(MarioBros.GROUND_BIT);

    }

    @Override
    public void onHeadHit() {

    }

    public void onFootHit() {
        Mario.jumpCount = 0;
    }
}
