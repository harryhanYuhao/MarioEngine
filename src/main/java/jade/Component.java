package jade;

import imgui.ImGui;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    public transient GameObject gameObject = null;

    public void start(){
    }
    public void update(float dt){}


    public void imgui(){
        // Component imgui: generate imgui with roll bar for each component of the game object
        try {
            Field [] fields = this.getClass().getDeclaredFields();
            for (Field field : fields){
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient)
                    continue;
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate){
                    field.setAccessible(true);
                }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class){
                    int val = (int) value;
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + ": ", imInt)){
                        field.set(this, imInt[0]);
                    }
                }
                else if (type == float.class){
                    float val = (float) value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + ": ", imFloat)){
                        field.set(this, imFloat[0]);
                    }
                } else if (type == boolean.class){
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val)){
                        val = !val;
                        field.set(this, val);
                    }
                } else if (type == Vector3f.class){
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat3(name + ": ", imVec)){
                        val.set(imVec[0], imVec[1], imVec[2]);
                        field.set(this, val);
                    }
                }

                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
