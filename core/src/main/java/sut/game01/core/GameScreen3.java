package sut.game01.core;
import static playn.core.PlayN.*;

import org.jbox2d.dynamics.World;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
//import sut.game01.core.Character.Militia;
import sut.game01.core.Character.Zealot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class GameScreen3 extends Screen {
    private  HomeScreen homeScreen;
    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer backButton;
    private World world;
    public GameScreen3(final ScreenStack ss) {
        this.ss = ss;
       // this.homeScreen = new HomeScreen(ss);
        Image bgImage = assets().getImage("images/bgGameplay.png");
        this.bgLayer = graphics().createImageLayer(bgImage);


        Image backImage = assets().getImage("images/main.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10 , 10);

        backButton.addListener(new Mouse.LayerAdapter(){

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(new HomeScreen(ss));
            }
        });

    }

   // Militia m = new Militia(world,560f,400f);

    public void wasShown() {
        super.wasShown();
      //  this.layer.add(bgLayer);
        this.layer.add(backButton);


     //   this.layer.add(m.layer());
    }

    @Override
    public void update(int delta){
        super.update(delta);

      //  m.update(delta);
    }


}
