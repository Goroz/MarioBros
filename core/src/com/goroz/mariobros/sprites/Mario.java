package com.goroz.mariobros.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.goroz.mariobros.MarioBros;
import com.goroz.mariobros.screen.PlayScreen;

/**
 * Created by HC Lim on 8/5/2016.
 */
public class Mario extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion marioStand;

    // Animation
    public enum State {
        FALLING, JUMPING, STANDING, RUNNING
    }

    public State currentState, previousState;
    private Animation marioRun, marioJump;
    private Boolean runningRight;
    private float stateTimer;
    // movement
    private int jumpCount = 0;
    private final Vector2 MOVE_VECTOR = new Vector2(0, 0);
    private Boolean canGoRight;
    private Boolean canGoLeft;
    private int mapWidth;
    private final float POINT_1 = 0.1f;

    public Mario(PlayScreen screen, int mapWidth) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = screen.getWorld();
        this.mapWidth = mapWidth;
        canGoLeft = true;
        canGoRight = true;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        jumpCount = 0;

        addAnimation();

        defineMario(world);
        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);
        setBounds(0, 0, 16 / MarioBros.PPM, 16 / MarioBros.PPM);
        setRegion(marioStand);
    }

    public void addAnimation() {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0, 16, 16));
        }
        marioRun = new Animation(POINT_1, frames);
        frames.clear();

        for (int i = 4; i < 6; i++) {
            frames.add(new TextureRegion(getTexture(), i * 16, 0,16, 16));
        }
        marioJump = new Animation(POINT_1, frames);
    }

    private void defineMario(World world) {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / MarioBros.PPM, 32 / MarioBros.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioBros.PPM);
        fdef.filter.categoryBits = MarioBros.MARIO_BIT;
        //what mario can collide with
        fdef.filter.maskBits = MarioBros.DEFAULT_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");
    }

    public void jump() {
        final int MAX_JUMP_COUNT = 2;
        if (jumpCount < MAX_JUMP_COUNT) {
            MOVE_VECTOR.set(0, 3.5f - jumpCount);
            b2body.applyLinearImpulse(MOVE_VECTOR, b2body.getWorldCenter(), true);
            jumpCount++;
            currentState = State.JUMPING;
        }
    }

    public void goRight() {
        float linear_velocity_x = b2body.getLinearVelocity().x;
        if (!canGoRight) {
            MOVE_VECTOR.set(-linear_velocity_x, 0);
            b2body.applyLinearImpulse(MOVE_VECTOR, b2body.getWorldCenter(), true);
        } else if (linear_velocity_x <= 2) {
            MOVE_VECTOR.set(POINT_1, 0);
            b2body.applyLinearImpulse(MOVE_VECTOR, b2body.getWorldCenter(), true);
        }
    }

    public void goLeft() {
        float linear_velocity_x = b2body.getLinearVelocity().x;
        if (!canGoLeft) {
            MOVE_VECTOR.set(-linear_velocity_x, 0);
            b2body.applyLinearImpulse(MOVE_VECTOR, b2body.getWorldCenter(), true);
        } else if (linear_velocity_x >= -2) {
            MOVE_VECTOR.set(-POINT_1, 0);
            b2body.applyLinearImpulse(MOVE_VECTOR, b2body.getWorldCenter(), true);
        }
    }

    public void checkCanMove() {
        final float MAP_BOUND_OFFSET = 0.3f;
        float x = b2body.getPosition().x;
        if (x < MAP_BOUND_OFFSET) {
            canGoLeft = false;
        } else if (x + MAP_BOUND_OFFSET > mapWidth * MarioBros.TILE_SIZE / MarioBros.PPM) {
            canGoRight = false;
        } else {
            canGoLeft = true;
            canGoRight = true;
        }
    }

    public void update(float delta) {
        float y = b2body.getLinearVelocity().y;
        checkCanMove();
        setPosition(b2body.getPosition().x - getWidth() / 2, (y - getHeight())/2 );
        setRegion(getFrame(delta));
        if (y == 0) {
            jumpCount = 0;
        }
    }

    public State getState() {
        float y = b2body.getLinearVelocity().y;
        if ((y > 0 && currentState == State.JUMPING) || (y < 0 && previousState == State.JUMPING)) {
            return State.JUMPING;
        } else if (y < 0) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = marioJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = marioRun.getKeyFrame(stateTimer, true);
                break;
            default:
                region = marioStand;
                break;

        }
        float x = b2body.getLinearVelocity().x;
        if ((x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;
        } else if ((x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;

        return region;
    }

}
