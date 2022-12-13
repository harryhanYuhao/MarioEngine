package jade;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
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
import imgui.*;
import imgui.gl3.ImGuiImplGl3;


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
        //io.getFonts().addFontDefault(); // Add default font for latin glyphs

        // You can use the ImFontGlyphRangesBuilder helper to create glyph ranges based on text input.
        // For example: for a game where your script is known, if you can feed your entire script to it (using addText) and only build the characters the game needs.
        // Here we are using it just to combine all required glyphs in one place
        final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
        rangesBuilder.addRanges(io.getFonts().getGlyphRangesDefault());
//        rangesBuilder.addRanges(io.getFonts().getGlyphRangesCyrillic());
//        rangesBuilder.addRanges(io.getFonts().getGlyphRangesJapanese());
        //rangesBuilder.addRanges(FontAwesomeIcons._IconRange);

        // Font config for additional fonts
        // This is a natively allocated struct so don't forget to call destroy after atlas is built
        final ImFontConfig fontConfig = new ImFontConfig();
        //fontConfig.setMergeMode(true);  // Enable merge mode to merge cyrillic, japanese and icons with default font

        final short[] glyphRanges = rangesBuilder.buildRanges();
//        io.getFonts().addFontFromMemoryTTF(loadFromResources("Tahoma.ttf"), 14, fontConfig, glyphRanges); // cyrillic glyphs
//        io.getFonts().addFontFromMemoryTTF(loadFromResources("NotoSansCJKjp-Medium.otf"), 14, fontConfig, glyphRanges); // japanese glyphs
//        io.getFonts().addFontFromMemoryTTF(loadFromResources("fa-regular-400.ttf"), 14, fontConfig, glyphRanges); // font awesome
//        io.getFonts().addFontFromMemoryTTF(loadFromResources("fa-solid-900.ttf"), 14, fontConfig, glyphRanges); // font awesome
        //io.getFonts().addFontFromFileTTF("assets/fonts/SegoeUI.ttf", 18, fontConfig, glyphRanges);

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
//        io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);  // Enable Multi-Viewport / Platform Windows

        // Enable Keyboard Controls
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);


        // Setup font

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, shall be destroyed

//        //Glyph could be added for per-font
//        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());
//
//        fontConfig.setMergeMode(false);
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/SegoeUI.ttf", 20, fontConfig);
        fontAtlas.build();

//
//
//
        fontConfig.destroy();

        //initFonts(ImGui.getIO());

        //ImFont font1 = ImGui.getIO().getFonts().addFontFromFileTTF("assets/fonts/SegoeUI.ttf", 18);

    }

    public void run(){
            imGuiGlfw.newFrame();
            ImGui.newFrame();
            ImGui.showDemoWindow();
            jade.ImGuiWindow.imgui();
            ImGui.render();
            imGuiGl3.renderDrawData(ImGui.getDrawData());


            // For Multiple View ports Does not seem to work
            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                final long backupWindowPtr = GLFW.glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                GLFW.glfwMakeContextCurrent(backupWindowPtr);
            }
    }

}
