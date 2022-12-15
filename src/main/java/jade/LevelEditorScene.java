package jade;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
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
        sprites = AssetPool.getSpritesheet("assets/images/decorationsAndBlocks.png");

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
        AssetPool.addSpritesheet("assets/images/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
    }

    @Override
    public void update(float dt){
        System.out.println(MouseListener.getOrthoX()+", "+MouseListener.getOrthoY());

        //System.out.println("FPS: " + 1.0f/dt);
        for (GameObject go : this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("Test window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTexCoords();

            ImGui.pushID(i); // otherwise all buttons would have the same ID
            // ImGui use the image's Id as their texture's id.

            if (ImGui.imageButton(id, spriteWidth, spriteHeight,
                    texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                System.out.println("Button " + i + "clicked");
            }

            ImGui.popID();
            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}

