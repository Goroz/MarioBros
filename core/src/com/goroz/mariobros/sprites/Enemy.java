package com.goroz.mariobros.sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.goroz.mariobros.screen.PlayScreen;

/**
 * Created by HC Lim on 25/6/2016.
 */
public abstract class Enemy extends Sprite {

    protected World world;
    protected Screen screen;
    public Body b2Body;

    public Enemy(PlayScreen screen,float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();

    }

    protected abstract void defineEnemy();
}
