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
import sut.game01.core.Character.InScreen3.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class ScreenWinner extends Screen {
    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,72);

    private HomeScreen homeScreen;
    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer backButton;


    public ScreenWinner(final ScreenStack ss) {
        this.ss = ss;
        // this.homeScreen = new HomeScreen(ss);
        Image bgImage = assets().getImage("images/win.png");
        this.bgLayer = graphics().createImageLayer(bgImage);


        Image backImage = assets().getImage("images/main.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10 , 10);

        backButton.addListener(new Mouse.LayerAdapter(){

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
            }
        });


    }


    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backButton);

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