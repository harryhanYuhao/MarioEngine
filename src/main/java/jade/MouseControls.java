package jade;

import org.lwjgl.glfw.GLFW;

public class MouseControls {
    GameObject holdingObject = null;

    public void pickUpObject(GameObject object){
        holdingObject = object;
        Window.getScene().addGameObjectToScene(object);
    }

    public void place() {
        holdingObject = null;
    }

    public void update(float dt) {
        if (holdingObject != null) {
            holdingObject.transform.position.x =
                MouseListener.getOrthoX() - 35;
            holdingObject.transform.position.y =
                MouseListener.getOrthoY() - 40;

            if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
