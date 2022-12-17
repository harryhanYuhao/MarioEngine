package scenes;

import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import jade.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.DebugDraw;
import util.AssetPool;
import util.Settings;

public class LevelEditorScene extends Scene {
    private GameObject obj1;
    private GameObject obj2;
    private Spritesheet sprites;
    SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();

    MouseControls mouseControls = new MouseControls();
    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    public LevelEditorScene(){
    }

    @Override
    public void init(){
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());

        loadResources();
        this.camera = new Camera(new Vector2f(0,0));
//        DebugDraw.addLine2D(new Vector2f(0,0), new Vector2f(800,800),
//                new Vector3f(1,0,0), 120);
        sprites = AssetPool.getSpritesheet("assets/images/decorationsAndBlocks.png");

        if (!levelLoaded) {
            obj2 = new GameObject("Object 1",
                    new Transform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);

            obj2SpriteRenderer.setColor(new Vector4f(1, 0, 0, 1));
            obj2.addComponent(obj2SpriteRenderer);


//            obj1 = new GameObject("Object 2",
//                    new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 0);
//            SpriteRenderer obj1SpriteRenderer = new SpriteRenderer();
//            obj1SpriteRenderer.setSprite(sprites.getSprite(0));
//
//            obj1.addComponent(obj1SpriteRenderer);

            obj2.addComponent(new Rigidbody());

            this.addGameObjectToScene(obj2);
//            this.addGameObjectToScene(obj1);
        }
        this.activeGameObject=gameObjects.get(0);

        }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpritesheet("assets/images/decorationsAndBlocks.png",
                new Spritesheet(AssetPool.getTexture("assets/images/decorationsAndBlocks.png"),
                        16, 16, 81, 0));
    }

    float rotation = 0;
    @Override
    public void update(float dt){

        levelEditorStuff.update(dt);
//        System.out.println(MouseListener.getOrthoX()+", "+MouseListener.getOrthoY());
        DebugDraw.addBox2D(new Vector2f(400,400),
                new Vector2f(32,64), rotation, new Vector3f(1,0,0), 1);
        rotation += dt;

        System.out.println("FPS: " + 1.0f/dt);
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

            //gabe switched texCoords[0].x and texCoords[2].x
            if (ImGui.imageButton(id, spriteWidth, spriteHeight,
                    texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, Settings.GRID_WIDTH, Settings.GRID_HEIGHT);
                // Attach to mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickUpObject(object);
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

