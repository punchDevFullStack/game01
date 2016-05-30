package sut.game01.core.Character.InScreen2;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.Layer;
import playn.core.PlayN;
import playn.core.util.Callback;
import playn.core.util.Clock;
import sut.game01.core.Character.InScreen1.Militia;
import sut.game01.core.Character.InScreen2.Bullet2Screen2;
import sut.game01.core.Screen.GameScreen2;
import sut.game01.core.sprite.Sprite;
import sut.game01.core.sprite.SpriteLoader;
import tripleplay.game.Screen;

public class HenchmanScreen2 extends Screen{
    // private GameScreen gameScreen = new GameScreen();
    private Militia militia;
    private Sprite sprite;
    private int si = 0;
    private boolean hasLoaded = false;
    private boolean checkContact = false;
    private boolean contacted;
    private int contactCheck;
    private Body other;
    private float x;
    private float y;
    private World world;
    private Bullet2Screen2 bullet2Screen2;

    public Body getBody() {
        return this.body;
    }


    public enum State {
        WALK
    }

    private State state = State.WALK;

    public Body body;

    private int e = 0;
    private int offset =0;
    public HenchmanScreen2(final World world, final float x_px, final float y_px) {
        this.x = x_px;
        this.y = y_px;
        this.world=world;

        sprite = SpriteLoader.getSprite("images/henchman.json");
        sprite.addCallback(new Callback<Sprite>() {

            @Override
            public void onSuccess(Sprite result) {
                sprite.setSprite(si);
                sprite.layer().setOrigin(
                        sprite.width() / 2f,
                        sprite.height() / 2f);
                sprite.layer().setTranslation(x, y + 13f);

                body = initPhysicsBody(world,
                        GameScreen2.M_PER_PIXEL * x,
                        GameScreen2.M_PER_PIXEL * y);

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
        bodyDef.type = BodyType.DYNAMIC;
        bodyDef.position = new Vec2(0, 0);
        Body body = world.createBody(bodyDef);



        PolygonShape shape = new PolygonShape();
        shape.setAsBox(54 * GameScreen2.M_PER_PIXEL / 2,
                sprite.layer().height() * GameScreen2.M_PER_PIXEL / 2);

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
        if(e > 150) {
            switch(state) {
                case WALK: offset = 0;
                    break;
            }
            si=offset+((si+1)%3);
            sprite.setSprite(si);
            e = 0;
        }
        if (checkContact == true)
            body.setActive(false);
    }

    @Override
    public void paint(Clock clock) {
        if (!hasLoaded) return;

        sprite.layer().setTranslation(
                (body.getPosition().x / GameScreen2.M_PER_PIXEL) ,
                body.getPosition().y / GameScreen2.M_PER_PIXEL);

        sprite.layer().setRotation(body.getAngle());

        switch (state){
            case WALK:
                body.applyForce(new Vec2(-5f, 0f), body.getPosition());
                break;

        }
    }


   /* public void shooting(){
        if (checkContact == false){
            bullet2Screen2 = new Bullet2Screen2(world,body.getPosition().x /GameScreen2.M_PER_PIXEL -150,body.getPosition().y / GameScreen2.M_PER_PIXEL-20);
            GameScreen2.shootHenchmanScreen2(bullet2Screen2);
        }else{

        }


    }*/
    public void contact(Contact contact){
        //body.setActive(false);
        checkContact = true;
        sprite.layer().setVisible(false);

    }
    public void contact2(Contact contact){
        contacted = true;
        contactCheck = 0;
        if(state == State.WALK ){
            state = State.WALK;
        }
        if(contact.getFixtureA().getBody()==body){
            other = contact.getFixtureB().getBody();
        }else{
            other = contact.getFixtureA().getBody();
        }
    }

}

