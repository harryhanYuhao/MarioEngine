package renderer;

import java.io.IOException;
import java.nio.file.Paths;

public class Shader {

    private int shaderProgramID;
    private String vertexSource;
    private String fragmentSource;
    private String filePath;

    public Shader (String filepath){
        this.filePath = filepath;
        try {
            String source = new String(java.nio.file.Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // Find the first pattern after #type
            int index = source.indexOf("#type")+6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // Find the second pattern after #type
            index = source.indexOf("#type", eol)+6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")){
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token '" + firstPattern + "'");
            }

            if (secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token '" + secondPattern + "'");
            }

        } catch (IOException e){
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath + "'";
        }

        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }

    public void compile(){

    }

    public void use(){

    }

    public void detach(){

    }
}
