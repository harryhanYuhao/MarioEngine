package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class Texture {
    private String filePath;
    private int texID;
    public Texture(String filepath){
        this.filePath = filepath;

        //Generate texture on GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //Set texture parameters
        //Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT); //S for x
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT); //T for y
        //When stretching the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        //When shrinking the image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        //Load image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer Channels = BufferUtils.createIntBuffer(1);
        //Load image and get width, height, and # of color channels
        ByteBuffer image = stbi_load(filepath, width, height, Channels, 0);

        if (image != null){
            if (Channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            } else if (Channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else {
                assert false : "Errors: (Texture) Image format not supported: unknown amount of color channels";
            }
            //glGenerateMipmap(GL_TEXTURE_2D);
        } else {
            assert false : "Error: Could not load image file: '" + filepath + "'";
        }

        stbi_image_free(image);
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

}
