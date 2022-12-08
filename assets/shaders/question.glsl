uniform sampler2D textures[32];

void main()
{
    float intensity = 1.0 / length(fs_in.position.xy - light_pos);

    vec4 texColor = fs_in.color;

    if(fs_in.tid > 0.0){
        int tid = int(fs_in.tid + 0.5);
        switch (int(tid)){
            case 0:
                texColor = texture(textures[0], fs_in.uv);
                break;
            case 1:
                texColor = texture(textures[1], fs_in.uv);
                break;
            case 2:
                texColor = texture(textures[2], fs_in.uv);
                break;
            case 3:
                texColor = texture(textures[3], fs_in.uv);
                break;
            case 4:
                texColor = texture(textures[4], fs_in.uv);
                break;
        }
    }

    color = texColor * intensity;
}
