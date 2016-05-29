package sut.game01.core.Character;


import static playn.core.PlayN.*;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Layer;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.*;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;
import tripleplay.game.Screen;

public class BulletScreen3 extends Screen {
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

    public enum State{
        IDLE
    };

    private char direction;
    private State state = State.IDLE;
    private int offset = 0;
    private int e = 0;
    public BulletScreen3(final World world, final float x_px, final float y_px, final char direction) {
        this.x = x_px;
        this.y = y_px;
        this.world = world;
        this.direction = direction;

        sprite = SpriteLoader.getSprite("images/bullet.json");
        sprite.addCallback(new Callback<Sprite>(){
            @Override
            public void onSuccess(Sprite result){
                sprite.setSprite(spriteIndex);
                sprite.layer().setOrigin(sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);
                body = initPhysicsBody(world, GameScreen3.M_PER_PIXEL * x_px,
                        GameScreen3.M_PER_PIXEL * y_px);

                hasLoaded = true;
            }

            @Override
            public void onFailure(Throwable cause){
                //PlayN.log().error("Error loading image!", cause);
            }

        });

    }

    private Body initPhysicsBody(World world, float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(x, y);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10 * GameScreen3.M_PER_PIXEL/2,
                10*GameScreen3.M_PER_PIXEL / 2);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 2000.0f;
        fixtureDef.restitution = 0.35f;
        body.createFixture(fixtureDef);

        body.setLinearDamping(0.2f);
        body.setTransform(new Vec2(x, y), 0f);
        body.applyLinearImpulse(new Vec2(10f,0f), body.getPosition());

        if(direction == 'R'){
            state = State.IDLE;
            // body.applyForce(new Vec2(10f,0f), body.getPosition());
        }else if(direction == 'L') {
            state =State.IDLE;
            // body.applyForce(new Vec2(-10f,0f), body.getPosition());
        }
        return body;
    }

    public Layer layer(){
        return sprite.layer();
    }

    public void update(int delta) {
        if (hasLoaded == false)
            return;

        e += delta;
        if(checkContact == true){
            body.setActive(false);
            checkContact = false;
        }

        if (e > 150) {
            switch (state) {
                case IDLE: offset = 0;
                    if(spriteIndex == 3){
                        state = State.IDLE;
                        //body.setActive(false);
                    }
                    break;

            }
            spriteIndex = offset + ((spriteIndex + 1) % 4);
            sprite.setSprite(spriteIndex);
            sprite.layer().setTranslation(body.getPosition().x / GameScreen3.M_PER_PIXEL,
                    body.getPosition().y / GameScreen3.M_PER_PIXEL);
            e = 0;
        }
    }

    public void paint(Clock clock){
        if(!hasLoaded) return;

        sprite.layer().setRotation(body.getAngle());

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen3.M_PER_PIXEL),
                body.getPosition().y / GameScreen3.M_PER_PIXEL);

    }

    public Body getBody(){
        return this.body;
    }
    public void contact(Contact contact){
        //body.setActive(false);
        checkContact = true;
        sprite.layer().setVisible(false);
    }


}

