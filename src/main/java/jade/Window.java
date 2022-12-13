package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private int width;
    private int height;
    final private String title;
    private long glfwWindow;

    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    public float a = 1.0f;
    private static Window window = null;

    private static Scene currentScene;
    private ImGuiLayer imGuiLayer;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Jade";
    } // constructor: setting the width, height, and title.

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.start();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.start();
                break;
            default:
                assert false : "Unknown scene" + newScene + "'";
                break;
        }
    } // changeScene(a): change to scene a. a is an integer up to 2.

    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run () {
        System.out.println("Hello LWJGL" + Version.getVersion()+"!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //Terminate GLFW and the free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        // Setup an error callback
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize glfw
        if (!glfwInit()){
            throw new IllegalStateException("Unable to Initialize GLFW.");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the Window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL){
            throw new IllegalStateException("Fail to create the GLEW Window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
        });

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-syne
        glfwSwapInterval(1);

        // make the window visable
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        // Create imGui
        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.init();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        Window.changeScene(0);
    }


    public void loop(){
        double beginTime = glfwGetTime();
        double endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            // poll events
            glfwPollEvents();

            glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
            glClear(GL_COLOR_BUFFER_BIT);

            if (currentScene != null){
                if (dt >= 0.0f){
                    currentScene.update(dt);
                    imGuiLayer.run();
                }
            }

            glfwSwapBuffers(glfwWindow);

            endTime = glfwGetTime();
            dt = (float)(endTime - beginTime);
            beginTime = endTime;
        }
    }

    public static int getHeight() {
        return get().height;
    }

    public static float getWidth() {
        return get().width;
    }

    public static void setWidth(int width) {
        get().width = width;
    }

public static void setHeight(int height) {
        get().height = height;
    }

}
