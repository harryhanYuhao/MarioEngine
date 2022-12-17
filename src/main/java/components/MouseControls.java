package components;

import components.Component;
import jade.GameObject;
import jade.MouseListener;
import jade.Window;
import org.lwjgl.glfw.GLFW;
import util.Settings;

public class MouseControls extends Component {
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
                MouseListener.getOrthoX() - 47;
            holdingObject.transform.position.y =
                MouseListener.getOrthoY() - 47;
            holdingObject.transform.position.x = ((int)((holdingObject.transform.position.x+22)/Settings.GRID_WIDTH) * Settings.GRID_WIDTH);
            holdingObject.transform.position.y = ((int)((holdingObject.transform.position.y+32)/Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT);
            if (MouseListener.mouseButtonDown(GLFW.GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}
