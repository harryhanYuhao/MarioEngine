package jade;

import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene(){
    }

    @Override
    public void init(){
        loadResources();

        this.camera = new Camera(new Vector2f(0,0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1",
                new Transform(new Vector2f(100,100), new Vector2f(256,256)), 1);
        obj1.addComponent(new SpriteRenderer(sprites.getSprite(0)));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2",
                new Transform(new Vector2f(400,100), new Vector2f(256,256)), 0);
        obj2.addComponent(new SpriteRenderer(sprites.getSprite(15)));
        this.addGameObjectToScene(obj2);

        //Object 1 Mario, 2 Mushroom

        this.activeGameObject=obj1;
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.25f;
    private float spriteFlipTimeLeft = 0.0f;
    @Override
    public void update(float dt){

        if (spriteFlipTimeLeft <= 0.0f){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            if (spriteIndex >= 4){
                spriteIndex = 0;
            }
            obj1.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex));
        } else {
            spriteFlipTimeLeft -= dt;
        }
        obj1.transform.position.x+= 300 * dt;

        System.out.println("FPS: " + 1.0f/dt);
        for (GameObject go : this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();

        if (obj1.transform.position.x > 1200){
            obj1.transform.position.x = 0;
        }
    }

    @Override
    public void imgui(){
        ImGui.begin("Level Editor");
        ImGui.text("Hello World");
        ImGui.end();
    }
}

