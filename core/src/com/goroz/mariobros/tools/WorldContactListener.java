package com.goroz.mariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.sprites.Enemies.Enemy;
import com.goroz.mariobros.sprites.TileObjects.Bricks;
import com.goroz.mariobros.sprites.TileObjects.Coins;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            case (MarioBros.ENEMY_HEAD_BIT | MarioBros.MARIO_BIT):
                if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_HEAD_BIT) {
                    ((Enemy) fixA.getUserData()).hitOnHead();
                } else {
                    ((Enemy) fixB.getUserData()).hitOnHead();
                }
                break;
            case (MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT):
                if (fixA.getFilterData().categoryBits == MarioBros.ENEMY_BIT) {
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                } else {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case (MarioBros.ENEMY_BIT | MarioBros.MARIO_BIT):
                Gdx.app.log("Mario", "DIED");
                break;
            case (MarioBros.ENEMY_BIT):
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case (MarioBros.MARIO_HEAD_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT):

                if (fixA.getFilterData().categoryBits == MarioBros.COIN_BIT) {
                    ((Coins) fixA.getUserData()).onHeadHit();
                } else {
                    ((Coins) fixB.getUserData()).onHeadHit();
                }
                break;
            case (MarioBros.MARIO_HEAD_BIT | MarioBros.BRICK_BIT):
                if (fixA.getFilterData().categoryBits == MarioBros.BRICK_BIT) {
                    ((Bricks) fixA.getUserData()).onHeadHit();
                } else {
                    ((Bricks) fixB.getUserData()).onHeadHit();
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
