package sut.game01.core.Screen;

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
//import sut.game01.core.Character.InScreen1.Militia;
import playn.core.util.Clock;

import sut.game01.core.Character.InScreen1.Swat;
import sut.game01.core.Character.InScreen2.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen2 extends Screen {
    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,72);

    ArrayList<Body> deletebody = new ArrayList<Body>();

  //  private final GameOverScreen loadGame;
    static int score;
    static int dead;
    static int numbullet;
    static  int numbullet3;
    private int count1 =0;
    private int count2 =0;
    private int count3 =0;
    private boolean h=false;
    private boolean m=false;
    private boolean p=false;
    private int bullet2Screen2counttime=0;
    private int bullet3Screen2counttime=0;
    private int bullet4Screen2counttime=0;

    private int i = 0;
    public static HashMap<Object,String> bodies;
    public static int k = 0;

    public  static void setNumbullet1(int numbullet){
        GameScreen2.numbullet3=numbullet;
    }
    public static int getNumbullet1(){
        return numbullet3;
    }
    public  static void setNumbullet3(int numbullet3){
        GameScreen2.numbullet3=numbullet3;
    }
    public static int getNumbullet3(){
        return numbullet3;
    }

    public enum Character{
        SWATSCREEN2,HENCHMANSCREEN2,MILITIASCREEN2,GUNSCREEN2,PENSIONERSCREEN2
    }
    private Character character;
    // define for screen
    private boolean destroy = false;
 //   private final GameOverScreen loadGame;
    private HomeScreen homeScreen;
    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer backButton;
    private SwatScreen2 swat2;  // use swat character
    private List<Swat> swat2List ; //  use swat in list
    private HenchmanScreen2 henchman2;
    private MilitiaScreen2 militiaScreen2;
    private GunScreen2 gun2;
    private static List<BulletScreen2> bulletScreen2List;
    private static List<Bullet2Screen2> bullet2Screen2List;
    private static List<Bullet3Screen2> bullet3Screen2List;
    private static List<Bullet4Screen2> bullet4Screen2List;
    private Bullet2Screen2 bullet2Screen2;
    private PensionerScreen2 pensionerScreen2;
    private Bullet3Screen2 bullet3Screen2;
    private Bullet4Screen2 bullet4Screen2;
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
       // this.loadGame = new GameOverScreen(ss);

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

        SwatScreen2.setNumbullet2(numbullet3);
        score= Integer.valueOf( GameScreen.score);
        dead=Integer.valueOf(GameScreen.dead);

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

        swat2 = new SwatScreen2(world, 80f, 400f);
        henchman2 = new HenchmanScreen2(world,440f,400f);
        bulletScreen2List =new ArrayList<BulletScreen2>();
        bullet2Screen2List =new ArrayList<Bullet2Screen2>();
        bullet3Screen2List=new ArrayList<Bullet3Screen2>();
        bullet4Screen2List=new ArrayList<Bullet4Screen2>();
        gun2 =  new GunScreen2(world,550f,300f);
        pensionerScreen2 = new PensionerScreen2(world,550f,400f);
        militiaScreen2 = new MilitiaScreen2(world,500f,400f);
    }



    public void wasShown() {

        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backButton);
        this.layer.add(swat2.layer());
        this.layer.add(henchman2.layer());
        this.layer.add(gun2.layer());
        this.layer.add(pensionerScreen2.layer());
        this.layer.add(militiaScreen2.layer());
        if(showDebugDraw){
            CanvasImage image = graphics().createImage(
                    (int)(width/GameScreen2.M_PER_PIXEL),
                    (int)(height/GameScreen2.M_PER_PIXEL));
            layer.add(graphics().createImageLayer(image));
            debugDraw= new DebugDrawBox2D();
            debugDraw.setCanvas(image);
            debugDraw.setFlipY(false);
            debugDraw.setStrokeAlpha(150);
            debugDraw.setFillAlpha(75);
            debugDraw.setStrokeWidth(2.0f);
            debugDraw.setFlags(DebugDraw.e_shapeBit|
                    DebugDraw.e_jointBit|DebugDraw.e_aabbBit);

            debugDraw.setCamera(0,0,1f/GameScreen2.M_PER_PIXEL);
         //   world.setDebugDraw(debugDraw);
        }
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body a = contact.getFixtureA().getBody();
                Body b = contact.getFixtureB().getBody();

                if (contact.getFixtureA().getBody() == swat2.getBody() ||
                        contact.getFixtureB().getBody() == swat2.getBody()) {
                    swat2.contact(contact);
                }
                if (contact.getFixtureA().getBody() == henchman2.getBody() ||
                        contact.getFixtureB().getBody() == henchman2.getBody()) {
                    henchman2.contact2(contact);
                }
                if(contact.getFixtureA().getBody()== militiaScreen2.getBody()||
                        contact.getFixtureB().getBody()== militiaScreen2.getBody()){
                    militiaScreen2.contact2(contact);
                }
                /**/
                if ((a == swat2.getBody() && b == henchman2.getBody()) || (a == henchman2.getBody() && b == swat2.getBody())) {
                    character = Character.SWATSCREEN2;
                    destroy = true;
                    swat2.layer().destroy();
                    ss.remove(ss.top());
                    ss.push(new GameOverScreen(ss));
                }
                if((a == swat2.getBody() &&  b == militiaScreen2.getBody()) || (a == militiaScreen2.getBody() && b == swat2.getBody())){
                    character = Character.SWATSCREEN2;
                    destroy=true;
                    swat2.layer().destroy();
                    ss.remove(ss.top());
                    ss.push(new GameOverScreen(ss));
                }
                if((a == swat2.getBody() &&  b == pensionerScreen2.getBody()) || (a == pensionerScreen2.getBody() && b == swat2.getBody())){
                    character = Character.SWATSCREEN2;
                    destroy=true;
                    swat2.layer().destroy();
                    ss.remove(ss.top());
                    ss.push(new GameOverScreen(ss));
                }
                if((a == swat2.getBody() &&  b == gun2.getBody()) || (a == gun2.getBody() && b == swat2.getBody())){
                    numbullet3=numbullet3+10;
                    character = Character.GUNSCREEN2;
                    destroy=true;
                    gun2.layer().destroy();
                }
                for(BulletScreen2 bu :bulletScreen2List){

                    if( contact.getFixtureA().getBody() == bu.getBody()||
                            contact.getFixtureB().getBody() == bu.getBody()){
                        bu.contact(contact);
                        deletebody.add(bu.body);
                    }
                    if((a == bu.getBody() &&  b == henchman2.getBody()) || (a == henchman2.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count1 = count1 +1;
                        if(count1==4) {
                            character = Character.HENCHMANSCREEN2;
                            destroy = true;h=destroy;
                            henchman2.layer().destroy();
                            henchman2.contact(contact);
                            score+=10;
                            count1=0;
                        }
                        deletebody.add(bu.body);
                        break;
                    }
                    if((a == bu.getBody() &&  b == militiaScreen2.getBody()) || (a == militiaScreen2.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count2=count2+1;
                        if(count2==4) {
                            character = Character.MILITIASCREEN2;
                            destroy = true; m=destroy;
                            militiaScreen2.layer().destroy();
                            count2=0;
                            score+=10;
                        }
                        deletebody.add(bu.body);
                        break;
                    }
                    if((a == bu.getBody() &&  b == pensionerScreen2.getBody()) || (a == pensionerScreen2.getBody() && b == bu.getBody())){
                        bu.contact(contact);
                        count3=count3+1;
                        if(count3==4) {
                            character = Character.PENSIONERSCREEN2;
                            destroy = true; p=destroy;
                            pensionerScreen2.layer().destroy();
                            count3=0;
                            score+=10;
                           // GameScreen2.setNumbullet1(numbullet);
                            ss.remove(ss.top());
                            ss.push(new GameScreen3(ss));
                        }
                        deletebody.add(bu.body);
                        break;
                    }
                }
                for(Bullet2Screen2 bu2 :bullet2Screen2List){
                    if( contact.getFixtureA().getBody() == bu2.getBody()||
                            contact.getFixtureB().getBody() == bu2.getBody()){
                        bu2.contact(contact);
                        deletebody.add(bu2.body);
                    }
                    if((a == bu2.getBody() &&  b == swat2.getBody()) || (a == swat2.getBody() && b == bu2.getBody())){
                        bu2.contact(contact);
                        dead=dead-1;
                        if(dead==0) {
                            character = Character.SWATSCREEN2;
                            destroy = true;
                            swat2.layer().destroy();
                            ss.remove(ss.top());
                            ss.push(new GameOverScreen(ss));
                        }
                        deletebody.add(bu2.body);
                        break;
                    }
                }
                for(Bullet3Screen2 bu3 :bullet3Screen2List){
                    if( contact.getFixtureA().getBody() == bu3.getBody()||
                            contact.getFixtureB().getBody() == bu3.getBody()){
                        bu3.contact(contact);
                        deletebody.add(bu3.body);
                    }
                    if((a == bu3.getBody() &&  b == swat2.getBody()) || (a == swat2.getBody() && b == bu3.getBody())){
                        bu3.contact(contact);
                        dead=dead-1;
                        if(dead==0) {
                            character = Character.SWATSCREEN2;
                            destroy = true;
                            swat2.layer().destroy();
                            ss.remove(ss.top());
                            ss.push(new GameOverScreen(ss));
                        }
                        deletebody.add(bu3.body);
                        break;
                    }
                }
                for(Bullet4Screen2 bu4 :bullet4Screen2List){
                    if( contact.getFixtureA().getBody() == bu4.getBody()||
                            contact.getFixtureB().getBody() == bu4.getBody()){
                        bu4.contact(contact);
                        deletebody.add(bu4.body);
                    }
                    if((a == bu4.getBody() &&  b == swat2.getBody()) || (a == swat2.getBody() && b == bu4.getBody())){
                        bu4.contact(contact);
                        dead=dead-1;
                        if(dead==0) {
                            character = Character.SWATSCREEN2;
                            destroy = true;
                            swat2.layer().destroy();
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
    public void update(int delta){
        GameOverScreen.setScore(score);

        super.update(delta);
        swat2.update(delta);
        henchman2.update(delta);
        gun2.update(delta);
        pensionerScreen2.update(delta);
        world.step(0.033f,10,10);
        GameScreen3.setNumbullet4(numbullet3);
        militiaScreen2.update(delta);
        for(BulletScreen2 bu : bulletScreen2List){
            bu.update(delta);
            this.layer.add(bu.layer());
        }
        for(Bullet2Screen2 bu2 : bullet2Screen2List){
            bu2.update(delta);
            this.layer.add(bu2.layer());
        }
        for(Bullet3Screen2 bu3 : bullet3Screen2List){
            bu3.update(delta);
            this.layer.add(bu3.layer());
        }
        for(Bullet4Screen2 bu4 : bullet4Screen2List){
            bu4.update(delta);
            this.layer.add(bu4.layer());
        }
        if (destroy==true){
            switch (character){
                case SWATSCREEN2: world.destroyBody(swat2.getBody()); break;
                case HENCHMANSCREEN2: world.destroyBody(henchman2.getBody()); break;
                case MILITIASCREEN2: world.destroyBody(militiaScreen2.getBody()); break;
                case GUNSCREEN2: world.destroyBody(gun2.getBody()); break;
            }
        }
        if(h==false){
            bullet2Screen2counttime += 20;
            System.out.println(bullet2Screen2counttime);

            if (bullet2Screen2counttime % 2000 == 0) {
                shooting();
            }
        }
        if(m==false){
            bullet3Screen2counttime +=10;
            System.out.println(bullet3Screen2counttime);

            if (bullet3Screen2counttime %2000 == 0) {
                shooting1();
            }
        }
        if(p==false){
            bullet4Screen2counttime +=10;
            System.out.println(bullet4Screen2counttime);

            if (bullet4Screen2counttime %2000 == 0) {
                shooting2();
            }
        }
        if(h==true){
            bullet2Screen2counttime=0; System.out.println(bullet2Screen2counttime);
        }
        if(m==true){
            bullet3Screen2counttime=0;System.out.println(bullet3Screen2counttime);
        }
        if(p==true){
            bullet4Screen2counttime=0;System.out.println(bullet4Screen2counttime);
        }
        for(Body body : deletebody) {
            world.destroyBody(body);
        }
    }

    @Override
    public void paint(Clock clock){
        super.paint(clock);
        swat2.paint(clock);
        henchman2.paint(clock);
        gun2.paint(clock);
        pensionerScreen2.paint(clock);
        militiaScreen2.paint(clock);
        for(BulletScreen2 bu : bulletScreen2List){
            bu.paint(clock);
        }
        for(Bullet2Screen2 bu2 : bullet2Screen2List){
            bu2.paint(clock);
        }
        for(Bullet3Screen2 bu3 : bullet3Screen2List){
            bu3.paint(clock);
        }
        for(Bullet4Screen2 bu4 : bullet4Screen2List){
            bu4.paint(clock);
        }
        if(showDebugDraw){
            debugDraw.getCanvas().clear();
            debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
            debugDraw.getCanvas().drawText("Score : "+String.valueOf(score),300f,50f);

            debugDraw.getCanvas().drawText("Bullet : " +numbullet3,500f,50f);
            debugDraw.getCanvas().drawText("Life : "+String.valueOf(dead),100f,50f);
            world.drawDebugData();
            if (numbullet3 == 0 ){
                debugDraw.getCanvas().setFillColor(Color.rgb(255, 255, 255));
                debugDraw.getCanvas().drawText("no bullet !!!",500f,90f);
            }
        }
    }
    public static void shootHenchmanScreen2(Bullet2Screen2 bullet2Screen2) {
        bullet2Screen2List.add(bullet2Screen2);
    }
    public static void shootMilitiaScreen2(Bullet3Screen2 bullet3Screen2) {
        bullet3Screen2List.add(bullet3Screen2);
    }
    public static void shootPensionerScreen2(Bullet4Screen2 bullet4Screen2) {
        bullet4Screen2List.add(bullet4Screen2);
    }
    public static void addBulletScreen2(BulletScreen2 bu){
        bulletScreen2List.add(bu);
    }
    public static void addBullet2Screen2(Bullet2Screen2 bu2Screen2){
        bullet2Screen2List.add(bu2Screen2);
    }
    public void shooting(){
            bullet2Screen2 = new Bullet2Screen2(world,henchman2.body.getPosition().x /GameScreen2.M_PER_PIXEL -150,henchman2.body.getPosition().y / GameScreen2.M_PER_PIXEL-20);
            GameScreen2.shootHenchmanScreen2(bullet2Screen2);
    }
    public void shooting1(){
        bullet3Screen2 = new Bullet3Screen2(world,militiaScreen2.body.getPosition().x / GameScreen2.M_PER_PIXEL-150 ,militiaScreen2.body.getPosition().y / GameScreen2.M_PER_PIXEL-20);
        GameScreen2.shootMilitiaScreen2(bullet3Screen2);
    }
    public void shooting2(){
        bullet4Screen2 = new Bullet4Screen2(world,pensionerScreen2.body.getPosition().x / GameScreen2.M_PER_PIXEL-150 ,pensionerScreen2.body.getPosition().y / GameScreen2.M_PER_PIXEL-20);
        GameScreen2.shootPensionerScreen2(bullet4Screen2);
    }
}
