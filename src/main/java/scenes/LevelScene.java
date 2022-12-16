package scenes;

import jade.KeyListener;
import jade.Window;
import scenes.Scene;

import java.awt.event.KeyEvent;

public class LevelScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 0.2f;
    public LevelScene(){
        System.out.println("LevelScene created");
    }

    @Override
    public void update(float dt){
            if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_W)){
                changingScene = true;
            }
            if (changingScene && Window.get().r<1.0f){
                timeToChangeScene -= dt;
                Window.get().r += dt*5.0f;
                Window.get().g += dt*5.0f;
                Window.get().b += dt*5.0f;
            } else if (changingScene){
                Window.changeScene(0);
                changingScene = false;
            }
    }
}

