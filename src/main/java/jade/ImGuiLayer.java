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

    public ImGuiLayer(long windowPtr){
        this.windowPtr = windowPtr;
    }

    public ImGuiLayer(ImGuiWindow ImGuiWindow) {
        this.ImGuiWindow = ImGuiWindow;
    }

    public void init(){
        initImGui();
        imGuiGlfw.init(windowPtr, true);
        imGuiGl3.init(glslVersion);
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }


    private void initImGui(){
        ImGui.createContext();
//        ImGuiIO io = ImGui.getIO();
//        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);  // Enable Multi-Viewport / Platform Windows
    }

    public void run(){

            imGuiGlfw.newFrame();
            ImGui.newFrame();
            //ImGui.showDemoWindow();
            jade.ImGuiWindow.imgui();
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }
    }

//    public static void execute() {
//        ImGuiLayer imguiLayer = new ImGuiLayer();
//        imguiLayer.init();
//        imguiLayer.run();
//        imguiLayer.destroy();
//    }
}
