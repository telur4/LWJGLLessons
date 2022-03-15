package app.game;

import app.engine.IGameLogic;
import app.engine.Window;

public class DummyGame implements IGameLogic {

    private float color = 0.0f;

    private final Renderer renderer;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
    }

    @Override
    public void render(Window window) {
        window.setClearColor(1.0f, color, color, 0.0f);
        renderer.clear();
    }
}
