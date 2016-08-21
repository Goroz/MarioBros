package com.goroz.mariobros.sprites;

import com.badlogic.gdx.audio.Sound;
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
 * Class about Mario
 */
public class Mario extends Sprite {
    public World world;
    public Body b2body;
    private MarioBros game;
    private TextureRegion marioStand, bigMarioStand, bigMarioJump, marioJump;

    // Animation
    public enum State {
        FALLING, JUMPING, STANDING, RUNNING, GROWING
    }

    private State currentState, previousState;
    private Animation marioRun, bigMarioRun, growMario;

    private Boolean runningRight;
    private float stateTimer;

    // movement
    public static int jumpCount = 0;
    private final Vector2 MOVE_VECTOR = new Vector2(0, 0);
    private Boolean canGoRight;
    private Boolean canGoLeft;
    private Boolean marioIsBig;
    private Boolean runGrowAnimation;
    private int mapWidth;
    private final float POINT_1 = 0.1f;

    public Mario(PlayScreen screen, int mapWidth, MarioBros game) {
        this.world = screen.getWorld();
        this.mapWidth = mapWidth;
        this.game = game;
        canGoLeft = true;
        canGoRight = true;
        runGrowAnimation = false;
        marioIsBig = false;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        jumpCount = 0;

        addAnimation(screen);


        marioStand = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 0, 0, MarioBros.TILE_SIZE, MarioBros.TILE_SIZE);
        bigMarioStand = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, MarioBros.TILE_SIZE, 2 * MarioBros.TILE_SIZE);
        setBounds(0, 0, MarioBros.TILE_SIZE / MarioBros.PPM, MarioBros.TILE_SIZE / MarioBros.PPM);
        setRegion(marioStand);
        defineMario(world);
    }

    public void addAnimation(PlayScreen screen) {
        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i * MarioBros.TILE_SIZE, 0, MarioBros.TILE_SIZE, MarioBros.TILE_SIZE));
        }
        marioRun = new Animation(POINT_1, frames);
        frames.clear();

        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i * MarioBros.TILE_SIZE, 0, MarioBros.TILE_SIZE, 2 * MarioBros.TILE_SIZE));
        }
        bigMarioRun = new Animation(POINT_1, frames);
        frames.clear();

        //get set animation frames from growing mario
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        growMario = new Animation(0.2f, frames);

        marioJump = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80, 0, MarioBros.TILE_SIZE, MarioBros.TILE_SIZE);
        bigMarioJump = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80, 0, MarioBros.TILE_SIZE, 2 * MarioBros.TILE_SIZE);
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
        fdef.filter.maskBits = MarioBros.DEFAULT_BIT | MarioBros.COIN_BIT | MarioBros.BRICK_BIT | MarioBros.GROUND_BIT | MarioBros.ENEMY_BIT | MarioBros.OBJECT_BIT | MarioBros.ENEMY_HEAD_BIT | MarioBros.ITEM_BIT;
        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / MarioBros.PPM, 6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, 6 / MarioBros.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        fdef.filter.categoryBits = MarioBros.MARIO_HEAD_BIT;
        b2body.createFixture(fdef).setUserData("head");

        EdgeShape foot = new EdgeShape();
        foot.set(new Vector2(-2 / MarioBros.PPM, -6 / MarioBros.PPM), new Vector2(2 / MarioBros.PPM, -6 / MarioBros.PPM));
        fdef.shape = foot;
        fdef.isSensor = true;
        fdef.filter.categoryBits = MarioBros.MARIO_FOOT_BIT;
        b2body.createFixture(fdef).setUserData("foot");
    }

    public void jump() {
        //can only jump 2 times consecutively
        final int MAX_JUMP_COUNT = 2;
        if (jumpCount < MAX_JUMP_COUNT) {
            // second jump is lower than first jump
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

    // Prevent mario move outside the map bound horizontally
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
        checkCanMove();
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(delta));

    }

    public State getState() {
        float y = b2body.getLinearVelocity().y;
        if (runGrowAnimation) {
            return State.GROWING;
        } else if ((y > 0 && currentState == State.JUMPING) || (y < 0 && previousState == State.JUMPING)) {
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
                region = marioIsBig ? bigMarioJump : marioJump;
                break;
            case RUNNING:
                region = marioIsBig ? bigMarioRun.getKeyFrame(stateTimer, true) : marioRun.getKeyFrame(stateTimer, true);
                break;
            case GROWING:
                region = growMario.getKeyFrame(stateTimer, false);
                if (growMario.isAnimationFinished(stateTimer)) {
                    runGrowAnimation = false;
                }
                break;
            default:
                region = marioIsBig ? bigMarioStand : marioStand;
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

    public void grow() {
        runGrowAnimation = true;
        marioIsBig = true;
        //change bound size as mario is now bigger
        setBounds(getX(), getY(), getWidth(), getHeight() * 2);
        game.manager.get("audio/sounds/powerup.wav", Sound.class).play();
    }

}
