package chapter05.p0.main.java.org.lwjglb.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import chapter05.p0.main.java.org.lwjglb.engine.IGameLogic;
import chapter05.p0.main.java.org.lwjglb.engine.Window;
import chapter05.p0.main.java.org.lwjglb.engine.graph.Mesh;

public class DummyGame implements IGameLogic {

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;

    private Mesh mesh;

    public DummyGame() {
        renderer = new Renderer();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        // 四角形の頂点リスト
        // しかし四角形を表すために座標を繰り返している
        // https://lwjglgamedev.gitbooks.io/3d-game-development-with-lwjgl/content/chapter05/quad_coordinates.png
        // 四角形の頂点をそれぞれ反時計回りに
        // V1, V2, V3, V4とするときそれぞれを
        // 0, 1, 2, 3とすると
        // それらの位置を参照して、これらの頂点を描画する順序を指定する
        // 0, 1, 3, 3, 1, 2の順で描画すればよい
        float[] positions = new float[] {
            // -0.5f,  0.5f, 0.0f,     // v1
            // -0.5f, -0.5f, 0.0f,     // v2
            //  0.5f,  0.5f, 0.0f,     // v4
            //  0.5f,  0.5f, 0.0f,     // v4
            // -0.5f, -0.5f, 0.0f,     // v2
            //  0.5f, -0.5f, 0.0f,     // v3

            //    0.0f / 2,    1.0f / 2, 0.0f,     // v1
            // -0.951f / 2,  0.309f / 2, 0.0f,     // v2
            // -0.587f / 2, -0.809f / 2, 0.0f,     // v3
            //  0.587f / 2, -0.809f / 2, 0.0f,     // v4
            //  0.951f / 2,  0.309f / 2, 0.0f,     // v5

               0.0f / 2,    1.0f / 2, 0.0f,     // v1   0
            -0.951f / 2,  0.309f / 2, 0.0f,     // v2   1
             0.951f / 2,  0.309f / 2, 0.0f,     // v5   4
             0.951f / 2,  0.309f / 2, 0.0f,     // v5   4
            -0.951f / 2,  0.309f / 2, 0.0f,     // v2   1
            -0.587f / 2, -0.809f / 2, 0.0f,     // v3   2
             0.951f / 2,  0.309f / 2, 0.0f,     // v5   4
            -0.587f / 2, -0.809f / 2, 0.0f,     // v3   2
             0.587f / 2, -0.809f / 2, 0.0f,     // v4   3

            //    0.0f / 2,    1.0f / 2, 0.0f,     // v1
            // -0.951f / 2,  0.309f / 2, 0.0f,     // v2
            // -0.587f / 2, -0.809f / 2, 0.0f,     // v3
            //  0.587f / 2, -0.809f / 2, 0.0f,     // v4
            //  0.951f / 2,  0.309f / 2, 0.0f,     // v5
            // -0.224f / 2,  0.309f / 2, 0.0f,     // v6
            // -0.362f / 2, -0.117f / 2, 0.0f,     // v7
            //    0.0f / 2, -0.381f / 2, 0.0f,     // v8
            //  0.362f / 2, -0.117f / 2, 0.0f,     // v9
            //  0.224f / 2,  0.309f / 2, 0.0f,     // v10

            //     0.0f / 2,    1.0f / 2, 0.0f,     // v1   0
            //  -0.224f / 2,  0.309f / 2, 0.0f,     // v6   5
            //   0.224f / 2,  0.309f / 2, 0.0f,     // v10  9

            //  -0.951f / 2,  0.309f / 2, 0.0f,     // v2   1
            //  -0.362f / 2, -0.117f / 2, 0.0f,     // v7   6
            //  -0.224f / 2,  0.309f / 2, 0.0f,     // v6   5

            //  -0.587f / 2, -0.809f / 2, 0.0f,     // v3   2
            //     0.0f / 2, -0.381f / 2, 0.0f,     // v8   7
            //  -0.362f / 2, -0.117f / 2, 0.0f,     // v7   6

            //   0.587f / 2, -0.809f / 2, 0.0f,     // v4   3
            //   0.362f / 2, -0.117f / 2, 0.0f,     // v9   8
            //     0.0f / 2, -0.381f / 2, 0.0f,     // v8   7

            //   0.951f / 2,  0.309f / 2, 0.0f,     // v5   4
            //   0.224f / 2,  0.309f / 2, 0.0f,     // v10  9
            //   0.362f / 2, -0.117f / 2, 0.0f,     // v9   8

            //  -0.224f / 2,  0.309f / 2, 0.0f,     // v6   5
            //  -0.362f / 2, -0.117f / 2, 0.0f,     // v7   6
            //   0.224f / 2,  0.309f / 2, 0.0f,     // v10  9

            //   0.224f / 2,  0.309f / 2, 0.0f,     // v10  9
            //  -0.362f / 2, -0.117f / 2, 0.0f,     // v7   6
            //     0.0f / 2, -0.381f / 2, 0.0f,     // v8   7
            
            //   0.224f / 2,  0.309f / 2, 0.0f,     // v10  9
            //     0.0f / 2, -0.381f / 2, 0.0f,     // v8   7
            //   0.362f / 2, -0.117f / 2, 0.0f,     // v9   8
        };
        mesh = new Mesh(positions);
    }

    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {           // 上アローキーを押したとき
            direction = 1;                                  // 位置を1上に
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {  // 下アローキーを押したとき
            direction = -1;                                 // 位置を1下に
        } else {                                            // 他のキーが押された場合
            direction = 0;                                  // 位置は変えない
        }
    }

    @Override
    public void update(float interval) {
        // directionの値によって色を変更
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        mesh.cleanUp();
    }

}
