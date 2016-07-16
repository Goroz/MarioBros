package com.goroz.mariobros.sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.screen.PlayScreen;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Pipes extends InteractiveTileObject {
    public Pipes(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoriesFilter(MarioBros.OBJECT_BIT);
    }


    @Override
    public void onHeadHit() {
        Gdx.app.log("pipes","hcollision");
    }

}
