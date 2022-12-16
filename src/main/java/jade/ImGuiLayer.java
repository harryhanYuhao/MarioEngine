package jade;

import imgui.ImFontConfig;
import imgui.ImGuiIO;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;

import imgui.*;
import scenes.Scene;


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

    private void initFonts(final ImGuiIO io) {
        //io.getFonts().addFontDefault(); // default font for latin glyphs

        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder();
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());

        final ImFontConfig fontConfig = new ImFontConfig();

        final short[] glyphRanges = rangesBuilder.buildRanges();
        // Add Three fonts and dispose the default: as the default is way to small
        io.getFonts().addFontFromFileTTF("assets/fonts/OpenSans-Regular.ttf", 18, fontConfig, glyphRanges);
        io.getFonts().addFontFromFileTTF("assets/fonts/OpenSans-Bold.ttf", 18, fontConfig, glyphRanges);
        io.getFonts().addFontFromFileTTF("assets/fonts/OpenSans-Light.ttf", 18, fontConfig, glyphRanges);
        io.getFonts().build();
        fontConfig.destroy();
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
    }


    private void initImGui(){
        ImGui.createContext();
        ImGuiIO io = ImGui.getIO();
        io.setIniFilename("imgui.ini");
//        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);  // Enable Multi-Viewport / Platform Windows

        // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);

        initFonts(io);
    }

    public void run(Scene currentScene){
            imGuiGlfw.newFrame();
            ImGui.newFrame();
            currentScene.sceneImGui();
            //ImGui.showDemoWindow();
            //jade.ImGuiWindow.imgui();
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());


            // For Multiple View ports Does not seem to work on linux
//            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
//                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
//                ImGui.updatePlatformWindows();
//                ImGui.renderPlatformWindowsDefault();
//                GLFW.glfwMakeContextCurrent(backupWindowPtr);
//            }
    }

}
