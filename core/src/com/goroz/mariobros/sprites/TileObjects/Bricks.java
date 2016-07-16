package com.goroz.mariobros.sprites.TileObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Rectangle;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.scenes.Hud;
import com.goroz.mariobros.screen.PlayScreen;
import com.goroz.mariobros.sprites.Mario;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Bricks extends InteractiveTileObject{
    private MarioBros game;

    public Bricks(PlayScreen screen, MapObject object, MarioBros game) {
        super(screen, object);
        this.game = game;
        fixture.setUserData(this);
        setCategoriesFilter(MarioBros.BRICK_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Bricks","collision");
        setCategoriesFilter(MarioBros.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        game.manager.get("audio/sounds/breakblock.wav", Sound.class).play();

    }

    public void onFootHit() {
        Mario.jumpCount = 0;
    }

}
