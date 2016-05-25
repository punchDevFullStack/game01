package sut.game01.core;
import static playn.core.PlayN.*;

import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import playn.core.*;
//import sut.game01.core.Character.Militia;
import playn.core.util.Clock;
import sut.game01.core.Character.Bullet;
import sut.game01.core.Character.Bullet2;
import sut.game01.core.Character.Swat;
import sut.game01.core.Character.Zealot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen2 extends Screen {
    private  HomeScreen homeScreen;
    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer backButton;
    private Swat swat;  // use swat character
    private List<Swat> swatList ; //  use swat in list

    //=======================================================
    // define for world

    public static float M_PER_PIXEL = 1 / 26.666667f ;
    private static int width = 24;
    private  static  int height = 18;

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;

    public GameScreen2(final ScreenStack ss) {
        this.ss = ss;
       // this.homeScreen = new HomeScreen(ss);
        Image bgImage = assets().getImage("images/bgGameScreen7.png");
        this.bgLayer = graphics().createImageLayer(bgImage);


        Image backImage = assets().getImage("images/main.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10 , 10);

        backButton.addListener(new Mouse.LayerAdapter(){

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(new HomeScreen(ss));
            }
        });

        //==================================================================
        // define world

        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);


        //==================================================================
        // insert ground in world

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height-1.4f), new Vec2(width+3.0f, height-1.4f ));
        ground.createFixture(groundShape, 0.0f);

        //==================================================================
        // insert left_wall in world
        EdgeShape left_wall = new EdgeShape();
        left_wall.set(new Vec2(0,0),new Vec2(0,height));
        ground.createFixture(left_wall,0.0f);
/*
        //==================================================================
        // insert right_wall in world
        EdgeShape right_wall = new EdgeShape();
        right_wall.set(new Vec2(24,0),new Vec2(24,height));
        ground.createFixture(right_wall,0.0f);
    */

        swat = new Swat(world, 80f, 400f);

        swatList = new ArrayList<Swat>(); // use arrayList
    }

   // Militia m = new Militia(world,560f,400f);

    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backButton);
        this.layer.add(swat.layer());

        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/GameScreen.M_PER_PIXEL),
                    (int)(height/GameScreen.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw= new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit|
                    DebugDraw.e_jointBit|DebugDraw.e_aabbBit);

            debugDraw.setCamera(0,0,1f/GameScreen.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }
     //   this.layer.add(m.layer());
    }

    @Override
    public void update(int delta){
        super.update(delta);
        swat.update(delta);
        world.step(0.033f,10,10);
      //  m.update(delta);
    }

    @Override
    public void paint(Clock clock){
        super.paint(clock);
        swat.paint(clock);
      /*  swat.paint(clock);
        henchman.paint(clock);
        militia.paint(clock);
        for(Bullet bu : bulletList){
            bu.paint(clock);
        }
        for(Bullet2 bu2 : bullet2List){
            bu2.paint(clock);
        }*/

        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
         //   debugDraw.getCanvas().drawText("Score : "+String.valueOf(GameScreen.score),300f,50f);
            world.drawDebugData();
        }
    }
}
