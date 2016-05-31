package sut.game01.core.Screen;

import org.jbox2d.dynamics.World;
import playn.core.*;
import playn.core.util.Clock;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.*;

public class GameOverScreen extends Screen {

    private final ScreenStack ss;
    private final ImageLayer bgLayer;
    private final ImageLayer retryLayer;
    private final ImageLayer mainLayer;
    private final ImageLayer scoreLayer;
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
    private boolean checkPoint = true;

    private Image number;
    private List<ImageLayer> scoreList1;
    private List<ImageLayer> scoreList2;

    public  static void setScore(int n){
        GameOverScreen.score=n;
    }

    public static final Font TITLE_FONT = graphics().createFont("Helvetica",Font.Style.PLAIN,36);
    JLabel scoreLabel = new JLabel("Score: 0");

    public GameOverScreen(final ScreenStack ss) {
        this.ss = ss;
        this.gameScreen2 = new GameScreen2(ss);

        Image bgImage = assets().getImage("images/over.png");
        this.bgLayer = graphics().createImageLayer(bgImage);

        Image retryImage = assets().getImage("images/retry.png");
        this.retryLayer = graphics().createImageLayer(retryImage);
      //  retryLayer.setTranslation(470, 325);

        Image mainImage = assets().getImage("images/main.png");
        this.mainLayer = graphics().createImageLayer(mainImage);
        mainLayer.setTranslation(120,325);

        Image scoreImage = assets().getImage("images/yourscore.png");
        this.scoreLayer = graphics().createImageLayer(scoreImage);
        scoreLayer.setTranslation(225,150);

    /*    retryLayer.addListener(new Mouse.LayerAdapter(

        ) {

            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.push(gameScreen2);
            }
        });*/

        mainLayer.addListener(new Mouse.LayerAdapter(){
            @Override
            public void onMouseDown(Mouse.ButtonEvent event) {
                ss.remove(ss.top());
                ss.remove(ss.top());
                ss.remove(ss.top());
                ss.push(new HomeScreen(ss));
            }
        });

        scoreList1 = new ArrayList<ImageLayer>();
        scoreList2 = new ArrayList<ImageLayer>();


        number = assets().getImage("images/0.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/1.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/2.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/3.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/4.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/5.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/6.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/7.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/8.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));
        number = assets().getImage("images/9.png");
        scoreList1.add(graphics().createImageLayer(number));
        scoreList2.add(graphics().createImageLayer(number));

    }


    public void wasShown() {
        super.wasShown();
        System.out.println(score);
        this.layer.add(bgLayer);
      //  this.layer.add(retryLayer);
        this.layer.add(mainLayer);
        this.layer.add(scoreLayer);

        for(ImageLayer l: scoreList1) {
            this.layer.add(l);
            l.setVisible(false);
        }
        for(ImageLayer l: scoreList2)  {
            this.layer.add(l);
            l.setVisible(false);
        }
        scoreList1.get(0).setTranslation(305f,15f);
        scoreList2.get(0).setTranslation(340f,15f);
        scoreList1.get(0).setVisible(true);
        scoreList2.get(0).setVisible(true);
}
    @Override
    public void update(int delta){
        super.update(delta);
    }
    @Override
    public void paint(Clock clock){
        super.paint(clock);

        checkPoint = true;
        checkNumber();

    }
    public void someoneScored()
    {
        scoreLabel.setBounds(10, 10, 100, 50);
        scoreLabel.setText("Score: " + score);
    }
    public void checkNumber(){
        int front, back;

        front = score/10;
        back = score%10;

        for(ImageLayer l: scoreList1)  l.setVisible(false);
        for(ImageLayer l: scoreList2)  l.setVisible(false);

        scoreList1.get(front).setTranslation(300f,220f);
        scoreList2.get(back).setTranslation(330f,220f);
        scoreList1.get(front).setVisible(true);
        scoreList2.get(back).setVisible(true);
        checkPoint = false;
    }
}
