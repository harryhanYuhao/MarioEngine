package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import imgui.ImGui;
import jade.Camera;
import jade.GameObject;
import jade.GameObjectDeserializer;
import renderer.Renderer;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();

    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<GameObject>();
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

    public Scene(){
    }

    public void init(){

    }

    public void start(){
        for (GameObject go: gameObjects){
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){
        if (!isRunning){
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }
    public abstract void update(float dt);

    public Camera camera(){
        return this.camera;
    }

    public void sceneImGui(){
        if (activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
        imgui();
    }

    public void imgui(){
    }

    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("assets/levels/level1.json");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("assets/levels/level1.json")));
        } catch (Exception e){
            e.printStackTrace();
        }

        if (!inFile.equals("")){
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] gameObjects = gson.fromJson(inFile, GameObject[].class);
            for (GameObject go: gameObjects){
                this.addGameObjectToScene(go);

                for (Component c : go.getAllComponents()){
                    if (c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }
                if (go.getUid() > maxGoId){
                    maxGoId = go.getUid();
                }
            }

            maxCompId++;
            maxGoId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
            this.levelLoaded = true;
        }
    }
}
