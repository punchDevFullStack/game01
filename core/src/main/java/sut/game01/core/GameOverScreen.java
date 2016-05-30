package sut.game01.core;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.callbacks.DebugDraw;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;
import playn.core.*;
import playn.core.util.Clock;
import sut.game01.core.Character.Bullet2Screen3;
import sut.game01.core.Character.BulletScreen3;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.*;

public class GameOverScreen extends Screen {

    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer retryLayer;
    private final ImageLayer mainLayer;
    private final GameScreen2 gameScreen2;

    static int score;

    //=======================================================
    // define for world
    public static float M_PER_PIXEL = 1 / 26.666667f ;
    private static int width = 24;
    private  static  int height = 1;
    private DebugDrawBox2D debugDraw;
    private boolean showDebugDraw = true;
    private World world;

    public GameOverScreen(final ScreenStack ss) {
        this.ss = ss;
        this.gameScreen2 = new GameScreen2(ss);

        Image bgImage = assets().getImage("images/over.png");
        this.bgLayer = graphics().createImageLayer(bgImage);

        Image retryImage = assets().getImage("images/retry.png");
        this.retryLayer = graphics().createImageLayer(retryImage);
        retryLayer.setTranslation(470, 325);

        Image mainImage = assets().getImage("images/main.png");
        this.mainLayer = graphics().createImageLayer(mainImage);
        mainLayer.setTranslation(120,325);

        retryLayer.addListener(new Mouse.LayerAdapter(

        ) {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(gameScreen2);
            }
        });

        mainLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });

        score = Integer.valueOf(GameScreen.score);

    }


    public void wasShown() {
        super.wasShown();

        this.layer.add(bgLayer);
        this.layer.add(retryLayer);
        this.layer.add(mainLayer);


}
    @Override
    public void update(int delta){
        super.update(delta);
    }
    @Override
    public void paint(Clock clock){
        super.paint(clock);

    }
}
