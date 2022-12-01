package jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    final private int width, height;
    final private String title;
    private long glfwWindow;

    public float r = 1.0f;
    public float g = 1.0f;
    public float b = 1.0f;
    public float a = 1.0f;
    public boolean fadeToBlack = false;
    private static Window window = null;

    private static Scene currentScene;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    } // constructor: setting the width, height, and title.

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                //currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
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

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable v-syne
        glfwSwapInterval(1);

        // make the window visable
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();

        Window.changeScene(0);
    }


    public void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            // poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (currentScene != null){
                if (dt >= 0.0f){
                    currentScene.update(dt);
                }
            }

            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
