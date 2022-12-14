package jade;

import components.*;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private GameObject obj2;
    private Spritesheet sprites;
    SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();

    public LevelEditorScene(){
    }

    @Override
    public void init(){
        loadResources();
        this.camera = new Camera(new Vector2f(0,0));
        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        if (!levelLoaded) {
            obj2 = new GameObject("Object 1",
                    new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);

            obj2SpriteRenderer.setColor(new Vector4f(1, 0, 0, 1));
            obj2.addComponent(obj2SpriteRenderer);


            obj1 = new GameObject("Object 2",
                    new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 0);
            SpriteRenderer obj1SpriteRenderer = new SpriteRenderer();
            obj1SpriteRenderer.setSprite(sprites.getSprite(0));

            obj1.addComponent(obj1SpriteRenderer);

            obj2.addComponent(new Rigidbody());

            this.addGameObjectToScene(obj2);
            this.addGameObjectToScene(obj1);
        }
        this.activeGameObject=gameObjects.get(0);
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
    }

    @Override
    public void update(float dt){

        //System.out.println("FPS: " + 1.0f/dt);
        for (GameObject go : this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){
//        ImGui.begin("Level Editor");
//        ImGui.text("Hello World");
//        ImGui.end();
    }
}

