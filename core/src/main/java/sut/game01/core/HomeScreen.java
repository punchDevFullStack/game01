package sut.game01.core;

import playn.core.*;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.ui.*;


import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;

public class HomeScreen extends Screen {
    private Sound music;
    private final ScreenStack ss;
    private final GameScreen3 gameScreen3;
    private final Setting setting;
    private final GameOverScreen loadGame;
    private final Howtoplay howtoplay;
    private final ImageLayer bgLayer;
    private final ImageLayer logoLayer;
    private final ImageLayer newButton;
    private final ImageLayer loadLayer;
    private final ImageLayer musicLayer;
   // private final ImageLayer soundLayer;
    private final ImageLayer questionLayer;
    private final ImageLayer settingLayer;
    private final GameScreen gameScreen;

    private Root root;

    public HomeScreen(final ScreenStack ss){
        this.ss = ss;
        this.gameScreen3 = new GameScreen3(ss);
        this.setting = new Setting(ss);
        this.loadGame = new GameOverScreen(ss);
        this.howtoplay = new Howtoplay(ss);
        this.gameScreen = new GameScreen(ss);

        music = assets().getSound("sounds/gun");
        music.setVolume(0.10f);    // set volume
       // music.setLooping(true);

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

        Image musicImage = assets().getImage("images/buttonSound.png");
        this.musicLayer = graphics().createImageLayer(musicImage);
        musicLayer.setTranslation(65, 410);

        Image questionImage = assets().getImage("images/buttonQuestion.png");
        this.questionLayer = graphics().createImageLayer(questionImage);
        questionLayer.setTranslation(120, 410);

        Image settingImage = assets().getImage("images/setting.png");
        this.settingLayer = graphics().createImageLayer(settingImage);
        settingLayer.setTranslation(175, 410);


        newButton.addListener(new Mouse.LayerAdapter() {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(gameScreen);
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

        questionLayer.addListener(new Mouse.LayerAdapter(){

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(howtoplay);
            }
        });
        musicLayer.addListener(new Mouse.LayerAdapter (){
            public ImageLayer soundLayer;

            public void onMouseDown(Mouse.ButtonEvent event){
            music.stop();
            Image soundImage = assets().getImage("images/buttonSoundStop.png");
            this.soundLayer = graphics().createImageLayer(soundImage);
            soundLayer.setTranslation(65, 410);
            graphics().rootLayer().add(soundLayer);
        }});
    }


    
    public void wasShown() {
        super.wasShown();

        music.play();

        this.layer.add(bgLayer);
        this.layer.add(logoLayer);
        this.layer.add(newButton);
        this.layer.add(loadLayer);
        this.layer.add(musicLayer);
       // this.layer.add(soundLayer);
        this.layer.add(questionLayer);
        this.layer.add(settingLayer);
    
    }
}
