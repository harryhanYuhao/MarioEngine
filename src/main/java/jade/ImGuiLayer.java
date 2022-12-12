package jade;

import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL32;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class ImGuiLayer {
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private String glslVersion = "#version 330";
    private long windowPtr;
    private ImGuiWindow ImGuiWindow;

    public ImGuiLayer(){

    }



    public ImGuiLayer(ImGuiWindow ImGuiWindow) {
        this.ImGuiWindow = ImGuiWindow;
    }

    public void init(){
        initWindow();
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(windowPtr);
        glfwDestroyWindow(windowPtr);
        glfwTerminate();
    }

    private void initWindow(){
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        windowPtr = glfwCreateWindow(1920, 1080, "Jade", MemoryUtil.NULL, MemoryUtil.NULL);

        if ( windowPtr == MemoryUtil.NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(windowPtr);
        glfwSwapInterval(1); // Enable v-sync
        glfwShowWindow(windowPtr); // Make the window visible

        GL.createCapabilities();
    }

    private void initImGui(){
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);  // Enable Multi-Viewport / Platform Windows
    }

    public void run(){
        while (!glfwWindowShouldClose(windowPtr)){
            glClearColor(0.2f, 0.19f, 0.2f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            imGuiGlfw.newFrame();
            ImGui.newFrame();
//            ImGui.showDemoWindow();
            ImGuiWindow.imgui();
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }

            GLFW.glfwSwapBuffers(windowPtr);
            GLFW.glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        ImGuiLayer imguiLayer = new ImGuiLayer(new ImGuiWindow());
        imguiLayer.init();
        imguiLayer.run();
        imguiLayer.destroy();
    }
}
