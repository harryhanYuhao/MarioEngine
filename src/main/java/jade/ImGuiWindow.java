package jade;

import imgui.ImGui;

public class ImGuiWindow {
    private boolean showText = false;
    public void imgui(){
        ImGui.begin("Hello, world!");

        if (ImGui.button("Show Text")) {
            showText = true;
        }

        if (showText) {
            ImGui.text("Hello, world!");
            ImGui.sameLine();
            if (ImGui.button("Hide Text")) {
                showText = false;
            }
        }

        ImGui.end();
    }

    public ImGuiWindow() {
    }
}
