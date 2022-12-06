package renderer;

import components.SpriteRenderer;
import jade.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch {
    // Vertex
    // =====
    //Pos                      Color
    //float, float,            float, float, float, float

    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE*Float.BYTES; // Float.BYTES = 4
    private final int VERTEX_SIZE = POS_SIZE + COLOR_SIZE;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE*Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;

    private int maxBatchSize;
    private Shader shader;

    public RenderBatch(int maxBatchSize){
        shader = AssetPool.getShader("assets/shaders/default.glsl");
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // 4 vertices quads
        this.vertices = new float[maxBatchSize*4*VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for the vertices on the GPU
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length *Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW); // Static draw: gl can optimize as it won't change

        // Enable the buffer attributes pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteRenderer spr){
        // Get Index and add renderObject
        int index = this.numSprites;
        // [0, 1, 2, 3, 4, 5]
        this.sprites[index] = spr;
        this.numSprites++;

        // Add properties to local vetices array
        loadVertexProperties(index);

        if (this.numSprites >= this.maxBatchSize){
            this.hasRoom = false;
        }
    }

    public void render(){
        // For now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());

        // Bind the VAO
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // Draw the sprites
        glDrawElements(GL_TRIANGLES, numSprites*6, GL_UNSIGNED_INT, 0);

        // Unbind the VAO
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // Unbind the shader
        shader.detach();
    }

    private void loadVertexProperties(int Index){
        SpriteRenderer sprite = this.sprites[Index];

        // Find offset within array (4 vertices per sprite)
        int offset = Index*4*VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertices with the appropriate properties

        float xAdd = 1.0f;
        float yAdd = 1.0f;

        for (int i = 0; i<4; i++){
            if (i==1){
                yAdd = 0.0f;
            } else if (i==2){
                xAdd = 0.0f;
            } else if (i==3){
                yAdd = 1.0f;
            }

            // load position
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset+1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            // load color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            offset += VERTEX_SIZE;
        }
    }

    private int[] generateIndices(){
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[maxBatchSize*6];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }
        return elements;
    }

    private void loadElementIndices(int[] elements, int index){
        int offsetArrayIndex = index*6;
        int offset = index*4;
        // 3, 2, 0, 0, 2, 1        7, 6, 4, 4, 6, 5
        // T
        elements[offsetArrayIndex+0] = offset+3;
        elements[offsetArrayIndex+1] = offset+2;
        elements[offsetArrayIndex+2] = offset+0;
        elements[offsetArrayIndex+3] = offset+0;
        elements[offsetArrayIndex+4] = offset+2;
        elements[offsetArrayIndex+5] = offset+1;
    }

    public boolean hasRoom(){
        return this.hasRoom;
    }

}
