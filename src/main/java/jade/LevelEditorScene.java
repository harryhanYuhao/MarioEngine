package jade;


import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {


    private float [] vertexArray = {
            // positions         // colors
             100f, -0.5f, 0.0f,   1.0f, 0.0f, 0.0f, 1.0f, // bottom right 0
             0f,  50f, 0.0f,   0.0f, 1.0f, 0.0f, 1.0f, // top left     1
             500.5f,  50f, 0.0f,   0.0f, 0.0f, 1.0f, 1.0f, // top right    2
             0f, -0.5f, 0.0f,   1.0f, 1.0f, 0.0f, 1.0f, // bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int [] elementArray = {
            2, 1, 0, // top right triangle
            0, 1, 3, // bottom left triangle
    };

    private int vaoID, vboID, eboID; // vao = vertex array object, vbo = vertex buffer object, ebo = element buffer object

    private Shader defaultshader;
    public LevelEditorScene(){

    }

    @Override
    public void init(){
        this.camera = new Camera(new Vector2f()); // Default camera position is (0, 0)
      defaultshader = new Shader("assets/shaders/default.glsl");
      defaultshader.compile_link();

        // ===============================================================
        // Generate VAO, VBO, and EBO, and send to GPU
        // VAO = Vertex Array Object, VBO = Vertex Buffer Object, EBO = Element Buffer Object
        // ===============================================================

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attributes pointer
        int positionSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colorSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionSize*floatSizeBytes);
        glEnableVertexAttribArray(1);


    }

    @Override
    public void update(float dt){
        camera.position.x -= 50.0f * dt;
        // Bind shader program
        defaultshader.use();
        defaultshader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultshader.uploadMat4f("uView", camera.getViewMatrix());

        // Bind VAO
        glBindVertexArray(vaoID);
        // Bind Vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the triangles
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultshader.detach();
    }
}

