package com.goroz.mariobros.sprites.Enemies;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
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
    public Vector2 velocity;

    public Enemy(PlayScreen screen,float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineEnemy();
        // Will now fall into hole
        velocity = new Vector2(-1, -1);
        b2Body.setActive(false);
    }

    protected abstract void defineEnemy();

    public abstract void hitOnHead();

    public abstract void update(float dt, PlayScreen screen);

    public void reverseVelocity(boolean x, boolean y) {
        if (x) {
            velocity.x = -velocity.x;
        }
        if (y) {
            velocity.y = -velocity.y;
        }
    }
}
