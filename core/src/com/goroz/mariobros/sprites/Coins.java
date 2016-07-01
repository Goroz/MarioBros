package com.goroz.mariobros.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.scenes.Hud;
import com.goroz.mariobros.screen.PlayScreen;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Coins extends InteractiveTileObject {

    private static TiledMapTileSet tileSet;
    //uniq ID of the tile
    private final int BLANK_COIN = 28;

    private MarioBros game;

    public Coins(PlayScreen screen,Rectangle bounds, MarioBros game) {
        super(screen, bounds);
        this.game = game;
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoriesFilter(MarioBros.COIN_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("coins", "collision");
        if(getCell().getTile().getId() == BLANK_COIN )
            game.manager.get("audio/sounds/bump.wav", Sound.class).play();
        else
            game.manager.get("audio/sounds/coin.wav", Sound.class).play();
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(100);

    }
}
