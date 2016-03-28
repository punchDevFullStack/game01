package sut.game01.core;

import playn.core.Font;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Mouse;
import react.UnitSlot;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.game.UIScreen;
import tripleplay.ui.*;
import tripleplay.ui.layout.AxisLayout;


import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class HomeScreen extends Screen {

    private final ScreenStack ss;
    private final GamePlay gamePlay;
    private final Setting setting;
    private final GameOverScreen loadGame;
    private final ImageLayer bgLayer;
    private final ImageLayer logoLayer;
    private final ImageLayer newButton;
    private final ImageLayer loadLayer;
    private final ImageLayer musicLayer;
    private final ImageLayer soundLayer;
    private final ImageLayer questionLayer;
    private final ImageLayer settingLayer;


    private Root root;

    public HomeScreen(final ScreenStack ss){
        this.ss = ss;
        this.gamePlay = new GamePlay(ss);
        this.setting = new Setting(ss);
        this.loadGame = new GameOverScreen(ss);




        Image bgImage = assets().getImage("images/bg.png");
        this.bgLayer = graphics().createImageLayer(bgImage);

        Image logoImage = assets().getImage("images/logo.png");
        this.logoLayer = graphics().createImageLayer(logoImage);
        logoLayer.setTranslation(10 , 30);
       
        Image newImage = assets().getImage("images/buttonNewGame.png");
        this.newButton = graphics().createImageLayer(newImage);
        newButton.setTranslation(30, 250);

        Image loadImage = assets().getImage("images/buttonLoadGame.png");
        this.loadLayer = graphics().createImageLayer(loadImage);
        loadLayer.setTranslation(30, 320);

        Image musicImage = assets().getImage("images/buttonMusic.png");
        this.musicLayer = graphics().createImageLayer(musicImage);
        musicLayer.setTranslation(12, 410);

        Image soundImage = assets().getImage("images/buttonSound.png");
        this.soundLayer = graphics().createImageLayer(soundImage);
        soundLayer.setTranslation(65, 410);

        Image questionImage = assets().getImage("images/buttonQuestion.png");
        this.questionLayer = graphics().createImageLayer(questionImage);
        questionLayer.setTranslation(120, 410);

        Image settingImage = assets().getImage("images/setting.png");
        this.settingLayer = graphics().createImageLayer(settingImage);
        settingLayer.setTranslation(175, 410);

        newButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(gamePlay);
            }
        });

        settingLayer.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(setting);
            }
        });

        loadLayer.addListener(new Mouse.LayerAdapter(){

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(loadGame);
            }
        });


    }


    
    public void wasShown() {
        super.wasShown();

        this.layer.add(bgLayer);
        this.layer.add(logoLayer);
        this.layer.add(newButton);
        this.layer.add(loadLayer);
        this.layer.add(musicLayer);
        this.layer.add(soundLayer);
        this.layer.add(questionLayer);
        this.layer.add(settingLayer);

    }
}
