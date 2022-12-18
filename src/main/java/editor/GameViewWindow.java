package editor;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiWindowFlags;

import jade.Window;

public class GameViewWindow {

    public static ImVec2 largestsize;
    public static ImVec2 centerposition;

    public static void imgui() {
        ImGui.begin("Game Viewport",
                 ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        largestsize = getLargestSizeForViewPort();
        centerposition = getCenteredPositionForViewPort(largestsize);

        ImGui.setCursorPos(centerposition.x, centerposition.y);

        int textureId = Window.getFrameBuffer().getTextureId();
        ImGui.imageButton(textureId, largestsize.x, largestsize.y, 0, 1, 1, 0);
        ImGui.end();
    }

    private static ImVec2 getCenteredPositionForViewPort(ImVec2 aspectSize) {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();


        float x = (windowSize.x - aspectSize.x) / 2;
        float y = (windowSize.y - aspectSize.y) / 2;
        return new ImVec2(x + ImGui.getCursorPosX(), y + ImGui.getCursorPosY());
    }

    private static ImVec2 getLargestSizeForViewPort() {
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();
        float width = windowSize.x;
        float height = width / Window.getTargetAspectRatio();
        float aspectRatio = Window.getTargetAspectRatio();
        if (height > windowSize.y) {
            height = windowSize.y;
            width = height * aspectRatio;
        }
        return new ImVec2(width, height);
    }
}
