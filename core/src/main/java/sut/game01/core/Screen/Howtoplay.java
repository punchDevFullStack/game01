package sut.game01.core.Screen;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class Howtoplay extends Screen {

    private final  ScreenStack ss;
    private final ImageLayer bgLayer;;
    private final ImageLayer backButton;
    private final ImageLayer howtoLayer;
  

    public Howtoplay(final ScreenStack ss) {
        this.ss = ss;


        Image bgImage = assets().getImage("images/bgHowtoplay.png");
        this.bgLayer = graphics().createImageLayer(bgImage);

        Image backImage = assets().getImage("images/backarrow.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(80 , 370);

        backButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
            }
        });

        Image howtoImage = assets().getImage("images/howtoplay.png");
        this.howtoLayer = graphics().createImageLayer(howtoImage);
        howtoLayer.setTranslation(80 , 100);
    }


    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backButton);
        this.layer.add(howtoLayer);
    }
}
