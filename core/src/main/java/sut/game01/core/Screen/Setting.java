package sut.game01.core.Screen;

import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class Setting extends Screen {

    private final  ScreenStack ss;
    private final ImageLayer bgLayer;;
    private final ImageLayer backButton;
    private final ImageLayer easyButton;
    private final ImageLayer normalButton;
    private final ImageLayer difficultButton;
  
    static boolean n=false;
    static boolean m=false;

    public Setting(final ScreenStack ss) {
        this.ss = ss;


        Image bgImage = assets().getImage("images/bgSetting.png");
        this.bgLayer = graphics().createImageLayer(bgImage);

        Image backImage = assets().getImage("images/backarrow.png");
        this.backButton = graphics().createImageLayer(backImage);
        backButton.setTranslation(100 , 325);

        backButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
            }
        });

        Image easyImage = assets().getImage("images/easy.png");
        this.easyButton = graphics().createImageLayer(easyImage);
        easyButton.setTranslation(230 , 160);

        easyButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                n=false;
                HomeScreen.setN(n);
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
            }
        });

        Image normalImage = assets().getImage("images/normal.png");
        this.normalButton = graphics().createImageLayer(normalImage);
        normalButton.setTranslation(230 , 220);

        normalButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                n=true;
                HomeScreen.setN(n);
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
            }
        });

        Image difficultImage = assets().getImage("images/dificult.png");
        this.difficultButton = graphics().createImageLayer(difficultImage);
        difficultButton.setTranslation(230 ,280);

        difficultButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                /*m=true;
                HomeScreen.setMM(m);
                ss.remove(ss.top());
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
                */
            }
        });
    }


    public void wasShown() {
        super.wasShown();
        this.layer.add(bgLayer);
        this.layer.add(backButton);
        this.layer.add(easyButton);
        this.layer.add(normalButton);
        this.layer.add(difficultButton);
    }
}
