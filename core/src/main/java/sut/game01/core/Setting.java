package sut.game01.core;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

/**
 * Created by Chatethakhun on 24/3/2559.
 */
public class Setting extends Screen {

    private final  ScreenStack ss;
    private final ImageLayer bgLayer;;
    private final ImageLayer backButton;
    private final ImageLayer titleLayer;
    private final ImageLayer sound;

    public Setting(final ScreenStack ss) {
        this.ss = ss;


        Image bgImage = assets().getImage("images/bg.png");
        this.bgLayer = graphics().createImageLayer(bgImage);

        Image backImage = assets().getImage("images/main.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(10 , 10);

        Image titileImage = assets().getImage("images/b.png");
        this.titleLayer = graphics().createImageLayer(titileImage);
        titleLayer.setTranslation(250 ,100 );

        Image soundImage = assets().getImage("images/sound.png");
        this.sound = graphics().createImageLayer(soundImage);
        sound.setTranslation(225 ,170 );


        backButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
            }
        });

    }


    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backButton);
        this.layer.add(titleLayer);
        this.layer.add(sound);

    }
}
