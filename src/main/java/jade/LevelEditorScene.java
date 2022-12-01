package jade;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {
    private boolean changingScene = false;
    private float timeToChangeScene = 0.2f;
    public LevelEditorScene(){
        System.out.println("LevelEditorScene created");
    }

    @Override
    public void update(float dt){
        System.out.printf("FPS "+1/dt+"\n");
        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)){
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0.0f){
            timeToChangeScene -= dt;
            Window.get().r -= dt*5.0f;
            Window.get().g -= dt*5.0f;
            Window.get().b -= dt*5.0f;
        } else if (changingScene){
            Window.changeScene(1);
            changingScene = false;
        }
    }
}

