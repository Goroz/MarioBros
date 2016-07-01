package com.goroz.mariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.scenes.Hud;
import com.goroz.mariobros.screen.PlayScreen;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Bricks extends InteractiveTileObject{
    private MarioBros game;
    public Bricks(PlayScreen screen, Rectangle bounds, MarioBros game) {
        super(screen, bounds);
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
}