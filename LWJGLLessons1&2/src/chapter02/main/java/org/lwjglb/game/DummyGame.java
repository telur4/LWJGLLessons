package chapter02.main.java.org.lwjglb.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.glViewport;
import chapter02.main.java.org.lwjglb.engine.IGameLogic;
import chapter02.main.java.org.lwjglb.engine.Window;

public class DummyGame implements IGameLogic {

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;
    
    public DummyGame() {
        // !
        renderer = new Renderer();
    }
    
    @Override
    public void init() throws Exception {
        renderer.init();
    }
    
    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {           // 上アローキーを押したとき
            direction = 1;                                  // RGB色を1上に
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {  // 下アローキーを押したとき
            direction = -1;                                 // RGB色を1下に
        } else {                                            // 他のキーが押された場合
            direction = 0;                                  // RGB色は変えない
        }
    }

    @Override
    public void update(float interval) {
        // directionの値によって色を変更
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        window.setClearColor(color, color, color, 0.0f);
        renderer.clear();
    }
}
