package com.goroz.mariobros.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.goroz.mariobros.MarioBros;

import com.goroz.mariobros.scenes.Hud;
import com.goroz.mariobros.sprites.Goomba;
import com.goroz.mariobros.sprites.Mario;
import com.goroz.mariobros.tools.B2WorldCreator;
import com.goroz.mariobros.tools.WorldContactListener;

/**
 * Created by HC Lim on 5/5/2016.
 */
public class PlayScreen implements Screen {
    //Reference to the game, used to set screen
    private MarioBros game;
    private TextureAtlas atlas;

    //Camera and hud
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;

    //Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private MapProperties mapProp;
    private int mapWidth;
    private OrthogonalTiledMapRenderer renderer;

    //Box2d variables
    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario player;
    private Goomba goomba;
    private Music music;

    public PlayScreen(MarioBros game) {
        this.game = game;
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        gameCam = new OrthographicCamera();
        //create a FitViewport to maintain aspect ratio
        gamePort = new FitViewport(MarioBros.V_WIDTH / MarioBros.PPM, MarioBros.V_HEIGHT / MarioBros.PPM, gameCam);
        //initially set the game cam to be centered correctly at the start of the map
        gameCam.position.set(gamePort.getWorldWidth()/2,gamePort.getWorldHeight()/2,0);

        // create game HUD for scores/timers/level infos
        hud = new Hud(game.batch);

        //load map and setup renderer
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioBros.PPM);
        mapProp = map.getProperties();

        mapWidth = mapProp.get("width", Integer.class);

        //world wih 0,0 gravity and sleep object at rest(box2d din compute sleeping objects)
        world = new World(new Vector2(0, -10), true);
        b2dr = new Box2DDebugRenderer();
        world.setContactListener(new WorldContactListener());

        //create environment and player
        new B2WorldCreator(this, game);
        player = new Mario(this, mapWidth);


        music = game.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.play();

        //goomba = new Goomba(this, .32f, .32f);
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.jump();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
           player.goRight();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.goLeft();
        }
    }

    public void update(float delta) {
        handleInput(delta);
        world.step(1 / 60f, 6, 2);
        player.update(delta);
//        goomba.update(delta);
        hud.update(delta);
        if(player.b2body.getPosition().x > gamePort.getWorldWidth()/2 && (player.b2body.getPosition().x + gamePort.getWorldWidth()/2) < mapWidth * 16 / MarioBros.PPM ) {
            gameCam.position.x = player.b2body.getPosition().x;

        }

        gameCam.update();
        renderer.setView(gameCam);
    }



    @Override
    public void render(float delta) {
        //separate update logic from render
        update(delta);


        //clear game screen with black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //render the map
        renderer.render();

        //render Box2DDebugLines
        b2dr.render(world, gameCam.combined);

        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        player.draw(game.batch);
        //goomba.draw(game.batch);
        game.batch.end();

        // Set batch to now draw what the HUD camera sees
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        atlas.dispose();
    }
}
