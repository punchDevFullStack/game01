package sut.game01.core;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import playn.core.CanvasImage;
import sut.game01.core.Character.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.*;

public class GameScreen extends Screen{
    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,72);
    private final GameScreen2 gameScreen2;
    private final GameOverScreen loadGame;
    private int life =3;
    private int score =0;
    private int count1 =0;
    private int count2 =0;
    public enum Character{
        SWAT,BULLET,BULLET2,HENCHMAN,MILITIA
    }
    private Character character;
    // define for screen
    private boolean destroy = false;
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;

    //=======================================================
    // define for character

    private Swat swat;  // use swat character
    private List<Swat> swatList ; //  use swat in list
    private Henchman henchman;
    private  Henchman henchman_1;
    private  Henchman henchman_2;
    private  Henchman henchman_3;
    private  List<Henchman> henchmanList;
    private Militia militia;
    private Militia militia_1;
    private Militia militia_2;
    private Militia militia_3;
    private  List<Militia> militiaList;
    private static List<Bullet> bulletList;
    private static List<Bullet2> bullet2List;
    //=======================================================
    // define

    private int i = 0;
    public static HashMap<Object,String> bodies;
    public static int k = 0;
    public static int point = 0;
    public static String debugString = "";
    public static String debugStringCoin = "";

    //=======================================================
    // define for world

    public static float M_PER_PIXEL = 1 / 26.666667f ;
    private static int width = 24;
    private  static  int height = 18;

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true ;

    //=======================================================

    public GameScreen(final ScreenStack ss) {
        this.ss = ss;
        this.gameScreen2 = new GameScreen2(ss);
        this.loadGame = new GameOverScreen(ss);
        Image bgImage = assets().getImage("images/bgGameScreen6.png");
        this.bg = graphics().createImageLayer(bgImage);

        //==================================================================
        // insert back button

        Image backImage = assets().getImage("images/main.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10,10);

        backButton.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseUp(Mouse.ButtonEvent event){
                ss.remove(ss.top());
            }
        });

        //==================================================================
        // define world

        Vec2 gravity = new Vec2(0.0f,10.0f);
        world = new World(gravity);
        world.setWarmStarting(true);
        world.setAutoClearForces(true);

        bodies =  new HashMap<Object, String>();
        bulletList = new ArrayList<Bullet>();
        bullet2List = new ArrayList<Bullet2>();
        //==================================================================
        // insert ground in world

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height-0.7f), new Vec2(width+3.0f, height-0.7f ));
        ground.createFixture(groundShape, 0.0f);
/*
        //==================================================================
        // insert left_wall in world
        EdgeShape left_wall = new EdgeShape();
        left_wall.set(new Vec2(0,0),new Vec2(0,height));
        ground.createFixture(left_wall,0.0f);

        //==================================================================
        // insert right_wall in world
        EdgeShape right_wall = new EdgeShape();
        right_wall.set(new Vec2(24,0),new Vec2(24,height));
        ground.createFixture(right_wall,0.0f);
*/


        //==================================================================
        // insert chis

        swat = new Swat(world, 80f, 400f);

        swatList = new ArrayList<Swat>(); // use arrayList
        henchman = new Henchman(world,490f,400f);
     //   henchman_1 = new Henchman(world,550f,400f);
     //   henchman_2 = new Henchman(world,550f,400f);
    //    henchman_3 = new Henchman(world,550f,400f);
        henchmanList = new ArrayList<Henchman>();
         militia = new Militia(world,570f,400f);
    //    militia_1 = new Militia(world,570f,400f);
   //     militia_2 = new Militia(world,570f,400f);
   //     militia_3 = new Militia(world,570f,400f);
        militiaList = new ArrayList<Militia>();

    }

    @Override
    public void wasShown(){
        super.wasShown();

        this.layer.add(bg);
        this.layer.add(backButton);
        this.layer.add(swat.layer());
        this.layer.add(henchman.layer());
        this.layer.add(militia.layer());

        //this.layer.add(henchman_1.layer());
        //============================================================
        // debug mode

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
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a =  contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if(contact.getFixtureA().getBody()==swat.getBody()||
                        contact.getFixtureB().getBody() == swat.getBody()){
                    swat.contact(contact);
                }
                if(contact.getFixtureA().getBody()==henchman.getBody()||
                        contact.getFixtureB().getBody() == henchman.getBody()){
                    henchman.contact2(contact);
                }
                if(contact.getFixtureA().getBody()== militia.getBody()||contact.getFixtureB().getBody()== militia.getBody()){
                    militia.contact2(contact);
                }

                if((a == swat.getBody() &&  b == henchman.getBody()) || (a == henchman.getBody() && b == swat.getBody())){
                    //swat.contact(contact);
                    //henchman.contact(contact);
                    character = Character.SWAT;
                    destroy=true;
                    swat.layer().destroy();
                    // bu.layer().destroy();
                    ss.push(loadGame);

                }
                if((a == swat.getBody() &&  b == militia.getBody()) || (a == militia.getBody() && b == swat.getBody())){
                    //swat.contact(contact);
                    //henchman.contact(contact);
                    character = Character.SWAT;
                    destroy=true;
                    swat.layer().destroy();
                    // bu.layer().destroy();
                    ss.push(loadGame);

                }
                for(Bullet bu :bulletList){

                 /*   if( contact.getFixtureA().getBody() == bu.getBody()||
                            contact.getFixtureB().getBody() == bu.getBody()){
                        bu.contact(contact);
                    }*/
                    if((a == bu.getBody() &&  b == henchman.getBody()) || (a == henchman.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count1 = count1 +1; //henchman.contact(contact);
                        System.out.println(count1);

                        if(count1==4) {
                            System.out.println("Die");
                            character = Character.HENCHMAN;
                            destroy = true;
                            henchman.layer().destroy();
                            // bu.layer().destroy();
                            ++score;
                            count1=0;
                           // henchman_1 = new Henchman(world,550f,400f);
                        }
                    }
                    if((a == bu.getBody() &&  b == militia.getBody()) || (a == militia.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count2=count2+1;
                        //henchman.contact(contact);

                        if(count2==4) {
                            character = Character.MILITIA;
                            destroy = true;
                            militia.layer().destroy();
                            // bu.layer().destroy();
                            count2=0;
                            ++score;
                       //     ss.push(new GameScreen2(ss));
                        }
                    }

                }
           /*     for(Bullet2 bu2 :bullet2List){
                    if((a == bu2.getBody() &&  b == swat.getBody()) || (a == swat.getBody() && b == bu2.getBody())){
                        bu2.contact(contact);
                        //henchman.contact(contact);
                        character = Character.SWAT;
                        destroy=true;
                        swat.layer().destroy();
                        // bu.layer().destroy();


                    }

                }*/
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold manifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse contactImpulse) {

            }
        });
    }

    @Override
    public void update (int delta) {
        super.update(delta);
        swat.update(delta);
        world.step(0.033f,10,10);
        henchman.update(delta);
        militia.update(delta);

       // henchman_1.update(delta);
        for(Bullet bu : bulletList){
            bu.update(delta);
            this.layer.add(bu.layer());
        }
        for(Bullet2 bu2 : bullet2List){
            bu2.update(delta);
            this.layer.add(bu2.layer());
        }
        if (destroy==true){
            switch (character){
                case SWAT: world.destroyBody(swat.getBody()); break;
               // case BULLET: world.destroyBody(bu.getBody()); break;
                case HENCHMAN: world.destroyBody(henchman.getBody()); break;
                case MILITIA: world.destroyBody(militia.getBody()); break;
            }
        }

    }

    @Override
    public void paint(Clock clock){
        super.paint(clock);

        swat.paint(clock);
        henchman.paint(clock);
        militia.paint(clock);

       // henchman_1.paint(clock);
        for(Bullet bu : bulletList){
            bu.paint(clock);
        }
        for(Bullet2 bu2 : bullet2List){
            bu2.paint(clock);
        }
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
            debugDraw.getCanvas().drawText("Score : "+String.valueOf(score),300f,50f);
            debugDraw.getCanvas().drawText("Life : "+String.valueOf(life),100f,50f);
            world.drawDebugData();
        }
    }
    public static void addBullet(Bullet bu){
        bulletList.add(bu);
    }
    public static void addBullet2(Bullet2 bu2){
        bullet2List.add(bu2);
    }
}


