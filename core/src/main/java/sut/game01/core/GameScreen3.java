package sut.game01.core;
import static playn.core.PlayN.*;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
//import sut.game01.core.Character.Militia;
import playn.core.util.Clock;
import sut.game01.core.Character.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import sut.game01.core.Character.SwatScreen3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen3 extends Screen {
    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,72);

    private  HomeScreen homeScreen;
    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer backButton;

    private SwatScreen3 swat3;
    private List<SwatScreen3> swatScreen3List;
    private Boss boss;
    private List<Boss> bossList;
    private static List<BulletScreen3> bulletScreen3List;
    private static List<Bullet2Screen3> bullet2Screen3List;
    //=======================================================
    // define for world

    public static float M_PER_PIXEL = 1 / 26.666667f ;
    private static int width = 24;
    private  static  int height = 18;

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;

    private int count1 =0;
    private boolean h=false;
    private boolean destroy = false;
    static int score;
    static int dead;
    static int numbullet6;
    static  int numbullet4;
    private int bullet2Screen3counttime=0;

    public enum Character{
        BOSS,SWATSCREEN3
    }
    private Character character;

    public  static void setNumbullet4(int numbullet){
        GameScreen3.numbullet6=numbullet;
    }
    public static int getNumbullet4(){
        return numbullet6;
    }
    public  static void setNumbullet6(int numbullet3){
        GameScreen3.numbullet6=numbullet3;
    }
    public static int getNumbullet6(){
        return numbullet6;
    }

    public GameScreen3(final ScreenStack ss) {
        this.ss = ss;
        // this.homeScreen = new HomeScreen(ss);
        Image bgImage = assets().getImage("images/bgGameScreen8.png");
        this.bgLayer = graphics().createImageLayer(bgImage);


        Image backImage = assets().getImage("images/main.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10 , 10);

        backButton.addListener(new Mouse.LayerAdapter(){

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(new HomeScreen(ss));
            }
        });

        SwatScreen3.setNumbullet5(numbullet6);
        score= Integer.valueOf( GameScreen2.score);
        dead=Integer.valueOf(GameScreen2.dead);
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

        swat3= new SwatScreen3(world,80f,400f);
        swatScreen3List = new ArrayList<SwatScreen3>();
        boss = new Boss(world,550f,400f);
        bulletScreen3List =new ArrayList<BulletScreen3>();
        bullet2Screen3List = new ArrayList<Bullet2Screen3>();
        bossList = new ArrayList<Boss>();

    }


    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backButton);
        this.layer.add(swat3.layer());
        this.layer.add(boss.layer());

        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/GameScreen3.M_PER_PIXEL),
                    (int)(height/GameScreen3.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw= new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit|
                    DebugDraw.e_jointBit|DebugDraw.e_aabbBit);

            debugDraw.setCamera(0,0,1f/GameScreen3.M_PER_PIXEL);
            world.setDebugDraw(debugDraw);
        }
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a =  contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if(contact.getFixtureA().getBody()==swat3.getBody()||
                        contact.getFixtureB().getBody() == swat3.getBody()){
                    swat3.contact(contact);
                }
                if (contact.getFixtureA().getBody() == boss.getBody() ||
                        contact.getFixtureB().getBody() == boss.getBody()) {
                    boss.contact2(contact);
                }
                for(BulletScreen3 bu :bulletScreen3List){

                    if( contact.getFixtureA().getBody() == bu.getBody()||
                            contact.getFixtureB().getBody() == bu.getBody()){
                        bu.contact(contact);
                    }
                    if((a == bu.getBody() &&  b == boss.getBody()) || (a == boss.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count1 = count1 +1; //henchman.contact(contact);
                        //System.out.println(count1);
                        // debugString = bodies.get(a) + " contact with " + bodies.get(b);
                        if(count1==5) {
                            character = Character.BOSS;
                            destroy = true;   h=destroy;
                            boss.layer().destroy();
                            // bu.layer().destroy();
                            boss.contact(contact);
                            score+=10;
                            count1=0;

                        }
                        break;
                    }
                }
                for(Bullet2Screen3 bu2 :bullet2Screen3List){
                    if((a == bu2.getBody() &&  b == swat3.getBody()) || (a == swat3.getBody() && b == bu2.getBody())){
                        bu2.contact(contact);
                        dead=dead-1;
                        if(dead==0) {
                            //henchman.contact(contact);
                            character = Character.SWATSCREEN3;
                            destroy = true;
                            swat3.layer().destroy();
                            // bu.layer().destroy();
                            ss.push(new GameOverScreen(ss));
                        }break;

                    }

                }
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
        });  }

    @Override
    public void update(int delta){
        super.update(delta);
        swat3.update(delta);
        boss.update(delta);
        world.step(0.033f,10,10);

        for(BulletScreen3 bu: bulletScreen3List){
            bu.update(delta);
            this.layer.add(bu.layer());
        }
        for(Bullet2Screen3 bu: bullet2Screen3List){
            bu.update(delta);
            this.layer.add(bu.layer());
        }
        if(h==false){
            bullet2Screen3counttime += 20;
            System.out.println(bullet2Screen3counttime);

            if (bullet2Screen3counttime % 2000 == 0) {
                boss.shooting();
            }
        }
        if(h==true){
            bullet2Screen3counttime=0; System.out.println(bullet2Screen3counttime);
        }
        if (destroy==true){
            switch (character){
                case SWATSCREEN3: world.destroyBody(swat3.getBody()); break;
             //   case HENCHMANSCREEN2: world.destroyBody(henchman2.getBody()); break;
                //   case MILITIASCREEN2: world.destroyBody(militia2.getBody()); break;
           //     case GUNSCREEN2: world.destroyBody(gun2.getBody()); break;
                case BOSS: world.destroyBody(boss.getBody()); break;
            }
        }
}
    @Override
    public void paint(Clock clock){
        super.paint(clock);
        swat3.paint(clock);
        boss.paint(clock);
    //   henchman.paint(clock);
      //militia.paint(clock);
        for(BulletScreen3 bu:bulletScreen3List){
            bu.paint(clock);
        }
        for(Bullet2Screen3 bu2 : bullet2Screen3List){
            bu2.paint(clock);
        }
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
            debugDraw.getCanvas().drawText("Score : "+String.valueOf(score),300f,50f);
            debugDraw.getCanvas().drawText("Bullet : " +numbullet6,500f,50f);
            debugDraw.getCanvas().drawText("Life : "+String.valueOf(dead),100f,50f);
            world.drawDebugData();
                if (numbullet6 == 0 ){
                    debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
                    debugDraw.getCanvas().drawText("no bullet !!!",500f,90f);
        }
    }
}
    public static void shootBossScreen3(Bullet2Screen3 bullet2) {
        bullet2Screen3List.add(bullet2);
    }
    public static void addBulletScreen3(BulletScreen3 bu)
    {
        bulletScreen3List.add(bu);
    }
    public static void addBullet2Screen3(Bullet2Screen3 bu2){
        bullet2Screen3List.add(bu2);
    }
}