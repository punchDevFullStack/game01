package sut.game01.core.Character.InScreen1;


import static playn.core.PlayN.*;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Layer;
import playn.core.util.Clock;
import sut.game01.core.Screen.GameScreen;
import sut.game01.core.sprite.Sprite;
import tripleplay.game.Screen;

public class Bullet4 extends Screen {
    private Sprite sprite;
    private int spriteIndex = 0;
    private boolean hasLoaded = false;
    private float x;
    private float y;
    private static Body body;
    private boolean contacted;
    private int contactCheck;
    private Body other;
    private World world;
    private boolean checkContact = false;
    private ImageLayer bullet4Layer;

    public enum State{
        IDLE
    };

    private State state = State.IDLE;
    private int offset = 0;
    private int e = 0;
    public Bullet4(final World world, final float x_px, final float y_px) {
        this.x = x_px;
        this.y = y_px;
        this.world = world;

        Image bullet4Image = assets().getImage("images/bullet3.png");
        bullet4Layer  = graphics().createImageLayer(bullet4Image);
        body = initPhysicsBody(world, GameScreen.M_PER_PIXEL * x_px,GameScreen.M_PER_PIXEL * y_px);

    }

    private Body initPhysicsBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(x, y);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 * GameScreen.M_PER_PIXEL/2,
                10*GameScreen.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1000.0f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(1.0f);
        body.setTransform(new Vec2(x, y), 0f);
        body.applyLinearImpulse(new Vec2(-5f,0f), body.getPosition());
        return body;
    }

    public Layer layer(){
        return bullet4Layer;
    }

    public void update(int delta) {
        if(checkContact == true){
            body.setActive(false);
            checkContact = false;
        }
    }

    public void paint(Clock clock){


        bullet4Layer.setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL),
                body.getPosition().y / GameScreen.M_PER_PIXEL);

    }

    public Body getBody(){
        return this.body;
    }
    public void contact(Contact contact){
        // body.setActive(false);
        checkContact = true;
        bullet4Layer.setVisible(false);
    }


}

