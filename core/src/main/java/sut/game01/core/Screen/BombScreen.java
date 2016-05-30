package sut.game01.core.Screen;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.*;

public class BombScreen extends Screen{
    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer retryLayer;
    private final ImageLayer mainLayer;
    private final GameScreen gameScreen;
    public BombScreen(final ScreenStack ss) {
        this.ss = ss;
        this.gameScreen = new GameScreen(ss);

        Image bgImage = assets().getImage("images/bomb.png");
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
                ss.push(gameScreen);
            }
        });

        mainLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });







    }


    public void wasShown() {
        super.wasShown();

        this.layer.add(bgLayer);
        this.layer.add(retryLayer);
        this.layer.add(mainLayer);

    }
}
