package sut.game01.core.Character;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.GameScreen;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;
import tripleplay.game.Screen;

public class Gun extends Screen{


    private Sprite sprite;

    private boolean hasLoaded = false;
    private boolean checkContact = false;
    private boolean contacted;
    private int contactCheck;
    private Body other;
    private float x;
    private float y;
    private World world;

    public Body getBody() {
        return this.body;
    }


    public enum State {
        IDLE
    }

    private State state = State.IDLE;
    private Body body;
    private int e = 0;
    private int si = 0;

    public Gun(final World world, final float x_px, final float y_px) {
        this.x = x_px;
        this.y = y_px;
        sprite = SpriteLoader.getSprite("images/gun.json");
        sprite.addCallback(new Callback<Sprite>() {

            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(si);
                sprite.layer().setOrigin(
                        sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);

                body = initPhysicsBody(world,
                        GameScreen.M_PER_PIXEL * x,
                        GameScreen.M_PER_PIXEL * y);

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause) {
                PlayN.log().error("Error loading image!", cause);
            }
        });


    }

    private Body initPhysicsBody(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.STATIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(80 * GameScreen.M_PER_PIXEL / 2,
                sprite.layer().height() * GameScreen.M_PER_PIXEL / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.4f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);
        body.setFixedRotation(true);
        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);

        return body;
    }

    public Layer layer() {
        return sprite.layer();
    }

    public void update(int delta) {
        if(hasLoaded == false) return;



        e += delta;
        if(e > 250) {
            switch(state) {
                case IDLE: if(si==0){
                    si = 0;
                } break;


                //  Bullet2 bu2 = new Bullet2(world,body.getPosition().x /GameScreen.M_PER_PIXEL +1,body.getPosition().y / GameScreen.M_PER_PIXEL-30);
                //GameScreen.addBullet2(bu2);break;
            }

            sprite.setSprite(si);
            e = 0;
        }
    }

    @Override
    public void paint(Clock clock) {
        if (!hasLoaded) return;

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen.M_PER_PIXEL) - 10,
                body.getPosition().y / GameScreen.M_PER_PIXEL);

        sprite.layer().setRotation(body.getAngle());

        switch (state){
            case IDLE:
                body.applyForce(new Vec2(-5f, 0f), body.getPosition());
                //Bullet2 bu2 = new Bullet2(world,body.getPosition().x /GameScreen.M_PER_PIXEL +100,body.getPosition().y / GameScreen.M_PER_PIXEL);
                //  GameScreen.addBullet2(bu2);
                break;

        }
    }

    public void contact(Contact contact){
        body.setActive(false);
        checkContact = true;
        sprite.layer().setVisible(false);


    }


}


