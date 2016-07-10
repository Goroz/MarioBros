package com.goroz.mariobros.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.sprites.Enemies.Enemy;
import com.goroz.mariobros.sprites.Enemies.Goomba;
import com.goroz.mariobros.sprites.TileObjects.InteractiveTileObject;
import com.goroz.mariobros.sprites.Mario;


/**
 * Created by HC Lim on 8/5/2016.
 */
public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        Object aUserData = fixA.getUserData();
        Object bUserData = fixA.getUserData();

        if(aUserData == "head" || bUserData == "head" ){
            Fixture head = aUserData == "head " ? fixA : fixB;
            Fixture object = head == fixB ? fixB : fixA;
            if(object.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }else if(aUserData == "foot" || bUserData == "foot" ){
            Fixture foot = aUserData == "foot" ? fixA : fixB;
            Fixture object = foot == fixB ? fixB : fixA;
            if(object.getUserData() instanceof InteractiveTileObject) {
                ((InteractiveTileObject) object.getUserData()).onFootHit();
            }else if (object.getUserData() instanceof Goomba){
                Mario.jumpCount = 0;
            }
        }

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
            case (MarioBros.ENEMY_BIT | MarioBros.ENEMY_BIT):
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
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
