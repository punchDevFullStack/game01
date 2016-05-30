package sut.game01.core.Screen;
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
import sut.game01.core.Character.InScreen1.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import sut.game01.core.Character.InScreen1.Swat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.*;

public class GameScreen extends Screen{
    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,72);
    private final GameScreen2 gameScreen2;
    private final GameOverScreen loadGame;

    ArrayList<Body> deletebody = new ArrayList<Body>();

    static int score = 0;
    static int numbullet=20;
    private int count1 =0;
    private int count2 =0;
    private int count3 =0;
    static   int dead=3;
    private boolean h=false;
    private  boolean m=false;
    private boolean p=false;

    public  static void setNumbullet(int numbullet){
      GameScreen.numbullet=numbullet;
  }

    public static int getNumbullet(){
        return numbullet;
    }

    public enum Character{
        SWAT,HENCHMAN,MILITIA,GUN,PENSIONER
    }
    private Character character;
    // define for screen
    private boolean destroy = false;
    private final ScreenStack ss;
    private final ImageLayer bg;
    private final ImageLayer backButton;

    //=======================================================
    // define for character
    private Gun gun;
    private Swat swat;  // use swat character
    private List<Swat> swatList ; //  use swat in list
    private Henchman henchman;
    private  List<Henchman> henchmanList;
    private Militia militia;
    private  List<Militia> militiaList;
    private Pensioner pensioner;
    private  List<Pensioner> pensionerList;
    private static List<Bullet> bulletList;
    private static List<Bullet2> bullet2List;
    private static List<Bullet3> bullet3List;
    private static List<Bullet4> bullet4List;
    private Bullet3 bullet3;
    private Bullet2 bullet2;
    private Bullet4 bullet4;

    //=======================================================
    // define
    private int check=0;
    private int i = 0;
    public static HashMap<Object,String> bodies;
    public static int k = 0;

    private int bullet2counttime=0;
    private int bullet3counttime=0;
    private int bullet4counttime=0;


    //=======================================================
    // define for world

    public static float M_PER_PIXEL = 1 / 26.666667f ;
    private static int width = 24;
    private  static  int height = 18;

    private World world;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw =true;

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
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
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
        bullet3List = new ArrayList<Bullet3>();
        bullet4List = new ArrayList<Bullet4>();

        //==================================================================
        // insert ground in world

        Body ground = world.createBody(new BodyDef());
        EdgeShape groundShape = new EdgeShape();
        groundShape.set(new Vec2(0, height-0.7f), new Vec2(width+3.0f, height-0.7f ));
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


        //==================================================================
        // insert chis

        swat = new Swat(world, 80f, 400f);

        swatList = new ArrayList<Swat>(); // use arrayList
        henchman = new Henchman(world,440f,400f);
        henchmanList = new ArrayList<Henchman>();
        militiaList = new ArrayList<Militia>();
        bullet2List =new ArrayList<Bullet2>();
        bullet3List =new ArrayList<Bullet3>();
        bullet4List =new ArrayList<Bullet4>();
        gun = new Gun(world,450f,260f);
        pensioner = new Pensioner(world,570f,400f);

        //   if(check==1){
        militia = new Militia(world,500f,400f);
        //   }
    }

    @Override
    public void wasShown(){
        super.wasShown();

        this.layer.add(bg);
        this.layer.add(backButton);
        this.layer.add(swat.layer());
        this.layer.add(henchman.layer());
        this.layer.add(pensioner.layer());
        this.layer.add(gun.layer());
      //  if(check==1) {
            this.layer.add(militia.layer());
     //   }

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
          //  world.setDebugDraw(debugDraw);
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

           /* */    if((a == swat.getBody() &&  b == henchman.getBody()) || (a == henchman.getBody() && b == swat.getBody())){
                    //swat.contact(contact);
                    //henchman.contact(contact);
                    character = Character.SWAT;
                    destroy=true;
                    swat.layer().destroy();
                    ss.remove(ss.top());
                    ss.push(loadGame);
                }
                if((a == swat.getBody() &&  b == militia.getBody()) || (a == militia.getBody() && b == swat.getBody())){
                    //swat.contact(contact);
                    //henchman.contact(contact);
                    character = Character.SWAT;
                    destroy=true;
                    swat.layer().destroy();
                    ss.remove(ss.top());
                    ss.push(loadGame);
                }
                if((a == swat.getBody() &&  b == pensioner.getBody()) || (a == pensioner.getBody() && b == swat.getBody())){
                    //swat.contact(contact);
                    //henchman.contact(contact);
                    character = Character.SWAT;
                    destroy=true;
                    swat.layer().destroy();
                    ss.remove(ss.top());
                    ss.push(loadGame);
                }
                if((a == swat.getBody() &&  b == gun.getBody()) || (a == gun.getBody() && b == swat.getBody())){
                    numbullet=numbullet+10;
                    character = Character.GUN;
                    destroy=true;
                    gun.layer().destroy();

                }
                for(Bullet bu :bulletList){

                    if( contact.getFixtureA().getBody() == bu.getBody()||
                            contact.getFixtureB().getBody() == bu.getBody()){
                        bu.contact(contact);
                        deletebody.add(bu.body);
                    }
                    if((a == bu.getBody() &&  b == henchman.getBody()) || (a == henchman.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count1 = count1 +1;
                        if(count1==3) {
                            character = Character.HENCHMAN;
                            destroy = true;h=destroy;
                            henchman.layer().destroy();
                            // bu.layer().destroy();
                            henchman.contact(contact);
                            score+=10;
                            count1=0;
                            check=1;
                        }
                        deletebody.add(bu.body);
                        break;
                    }
                    if((a == bu.getBody() &&  b == militia.getBody()) || (a == militia.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count2=count2+1;
                        if(count2==3) {
                            character = Character.MILITIA;
                            destroy = true; m=destroy;
                            militia.layer().destroy();
                            // bu.layer().destroy();
                            count2=0;
                            score+=10;
                        }
                        deletebody.add(bu.body);
                        break;
                    }
                    if((a == bu.getBody() &&  b == pensioner.getBody()) || (a == pensioner.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count3=count3+1;
                        if(count3==3) {
                            character = Character.PENSIONER;
                            destroy = true; p=destroy;
                            pensioner.layer().destroy();
                            // bu.layer().destroy();
                            count3=0;
                            score+=10;
                            GameScreen2.setNumbullet1(numbullet);
                            ss.remove(ss.top());
                            ss.push(new GameScreen2(ss));
                        }
                        deletebody.add(bu.body);
                        break;
                    }
                }
                for(Bullet2 bu2 :bullet2List){
                    if( contact.getFixtureA().getBody() == bu2.getBody()||
                            contact.getFixtureB().getBody() == bu2.getBody()){
                        bu2.contact(contact);
                        deletebody.add(bu2.body);
                    }
                    if((a == bu2.getBody() &&  b == swat.getBody()) || (a == swat.getBody() && b == bu2.getBody())){
                        bu2.contact(contact);
                        dead=dead-1;
                        if(dead==0) {
                            //henchman.contact(contact);
                            character = Character.SWAT;
                            destroy = true;
                            swat.layer().destroy();
                            ss.remove(ss.top());
                            ss.push(new GameOverScreen(ss));
                        }
                        deletebody.add(bu2.body);
                        break;
                    }
                }
                for(Bullet3 bu3 :bullet3List){
                    if( contact.getFixtureA().getBody() == bu3.getBody()||
                            contact.getFixtureB().getBody() == bu3.getBody()){
                        bu3.contact(contact);
                        deletebody.add(bu3.body);
                    }
                    if((a == bu3.getBody() &&  b == swat.getBody()) || (a == swat.getBody() && b == bu3.getBody())){
                        bu3.contact(contact);
                        dead=dead-1;
                        if(dead==0) {
                            //henchman.contact(contact);
                            character = Character.SWAT;
                            destroy = true;
                            swat.layer().destroy();
                            ss.remove(ss.top());
                            ss.push(new GameOverScreen(ss));
                        }
                        deletebody.add(bu3.body);
                        break;
                    }
                }
                for(Bullet4 bu4 :bullet4List){
                    if( contact.getFixtureA().getBody() == bu4.getBody()||
                            contact.getFixtureB().getBody() == bu4.getBody()){
                        bu4.contact(contact);
                        deletebody.add(bu4.body);
                    }
                    if((a == bu4.getBody() &&  b == swat.getBody()) || (a == swat.getBody() && b == bu4.getBody())){
                        bu4.contact(contact);
                        dead=dead-1;
                        if(dead==0) {
                            character = Character.SWAT;
                            destroy = true;
                            swat.layer().destroy();
                            ss.remove(ss.top());
                            ss.push(new GameOverScreen(ss));
                        }
                        deletebody.add(bu4.body);
                        break;
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
        });
    }

    @Override
    public void update (int delta) {
        GameOverScreen.setScore(score);

        super.update(delta);
        swat.update(delta);
        world.step(0.033f,10,10);
        henchman.update(delta);
        gun.update(delta);
        pensioner.update(delta);
      //  if(check==0) {
            militia.update(delta);  // henchman_1.update(delta);
       // }

        for(Bullet bu : bulletList){
            bu.update(delta);
            this.layer.add(bu.layer());
        }
        for(Bullet2 bu2 : bullet2List){
            bu2.update(delta);
            this.layer.add(bu2.layer());
        }
        for(Bullet3 bu3 : bullet3List){
            bu3.update(delta);
            this.layer.add(bu3.layer());
        }
        for(Bullet4 bu4 : bullet4List){
            bu4.update(delta);
            this.layer.add(bu4.layer());
        }
        if (destroy==true){
            switch (character){
                case SWAT: world.destroyBody(swat.getBody()); break;
                case HENCHMAN: world.destroyBody(henchman.getBody()); break;
                case MILITIA: world.destroyBody(militia.getBody()); break;
                case GUN: world.destroyBody(gun.getBody()); break;
                case PENSIONER: world.destroyBody(pensioner.getBody()); break;
            }
        }

            if(h==false){
                bullet2counttime += 20;
                System.out.println(bullet2counttime);

                if (bullet2counttime % 2000 == 0) {
                    shooting();
                }
            }
            if(m==false){
                bullet3counttime +=10;
                System.out.println(bullet3counttime);

                if (bullet3counttime %2000 == 0) {
                 shooting1();
                }
            }
        if(p==false){
            bullet4counttime +=10;
            System.out.println(bullet4counttime);

            if (bullet4counttime %2000 == 0) {
                shooting2();
            }
        }
            if(h==true){
                bullet2counttime=0; System.out.println(bullet2counttime);
            }
            if(m==true){
                bullet3counttime=0;System.out.println(bullet3counttime);
            }
            if(p==true){
                bullet4counttime=0;System.out.println(bullet4counttime);
            }
        for(Body body : deletebody) {
            world.destroyBody(body);
        }
    }



    @Override
    public void paint(Clock clock){
        super.paint(clock);

        swat.paint(clock);
        henchman.paint(clock);
        gun.paint(clock);
        pensioner.paint(clock);
       // if(check==0) {
            militia.paint(clock);
    //    }
        // henchman_1.paint(clock);
        for(Bullet bu : bulletList){
            bu.paint(clock);
        }
        for(Bullet2 bu2 : bullet2List){
            bu2.paint(clock);
        }
        for(Bullet3 bu3 : bullet3List){
            bu3.paint(clock);
        }
        for(Bullet4 bu4 : bullet4List){
            bu4.paint(clock);
        }
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
            debugDraw.getCanvas().drawText("Score : " + String.valueOf(score),300f,50f);
            debugDraw.getCanvas().drawText("Bullet : " +numbullet,500f,50f);
            debugDraw.getCanvas().drawText("Life : "+String.valueOf(dead),100f,50f);
            world.drawDebugData();
            if (numbullet == 0 ){
                debugDraw.getCanvas().drawText("no bullet !!!",500f,90f);
            }
        }
    }
    public static void shootHenchman(Bullet2 bullet2) {
        bullet2List.add(bullet2);
    }
    public static void shootPensioner(Bullet4 bullet4) {
        bullet4List.add(bullet4);
    }
    public static void shootMilitia(Bullet3 bullet3) {
        bullet3List.add(bullet3);
    }
    public static void addBullet(Bullet bu){
        bulletList.add(bu);
    }
    public static void addBullet2(Bullet2 bu2){
        bullet2List.add(bu2);
    }
    public static void addBullet3(Bullet3 bu3){
        bullet3List.add(bu3);
    }
    public void shooting() {
        bullet2 = new Bullet2(world, henchman.body.getPosition().x / GameScreen.M_PER_PIXEL - 150, henchman.body.getPosition().y / GameScreen.M_PER_PIXEL - 20);
        GameScreen.shootHenchman(bullet2);
    }
    public void shooting1(){
        bullet3 = new Bullet3(world,militia.body.getPosition().x / GameScreen.M_PER_PIXEL-150 ,militia.body.getPosition().y / GameScreen.M_PER_PIXEL-20);
        GameScreen.shootMilitia(bullet3);
    }
    public void shooting2(){
        bullet4 = new Bullet4(world,pensioner.body.getPosition().x / GameScreen.M_PER_PIXEL-150 ,pensioner.body.getPosition().y / GameScreen.M_PER_PIXEL-20);
        GameScreen.shootPensioner(bullet4);
    }
}


/*      int count
        enemy dead -> count = 1;
*       if count = 1
*           new Enemy
 *           count = 2
 *          add -> layer  -> update
 *          for loop
 *              paint
 *              update
 *
*
*
*
* */


