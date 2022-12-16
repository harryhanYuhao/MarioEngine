package components;

public class FontRenderer extends Component {

    private boolean firstTime = false;
    @Override
    public void start(){
        if (gameObject.getComponent(SpriteRenderer.class) != null){
            System.out.println("Found font renderer!");
        } else {
            System.out.println("No font renderer found!");
        }
    }

    @Override
    public void update(float dt) {
        if (!firstTime){
            System.out.println("I am updating!");
            firstTime = true;
        }
    }
}

