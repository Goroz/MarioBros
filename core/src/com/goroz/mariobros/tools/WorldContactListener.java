package com.goroz.mariobros.tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.goroz.mariobros.sprites.Goomba;
import com.goroz.mariobros.sprites.InteractiveTileObject;
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
