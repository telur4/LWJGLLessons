package chapter05.p2.main.java.org.lwjglb.game;

import chapter05.p2.main.java.org.lwjglb.engine.Utils;
import chapter05.p2.main.java.org.lwjglb.engine.Window;
import chapter05.p2.main.java.org.lwjglb.engine.graph.Mesh;
import chapter05.p2.main.java.org.lwjglb.engine.graph.ShaderProgram;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

public class Renderer {

    private ShaderProgram shaderProgram;

    public Renderer() {
    }

    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        // 頂点シェーダーとフラグメントシェーダーのコードファイルをロードします。
        shaderProgram.createVertexShader(Utils.loadResource("../../../../resources/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("../../../../resources/fragment.fs"));
        shaderProgram.link();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    // meshでデータが既にGPUにある状態なので、
    // あとはレンダリングして表示する
    public void render(Window window, Mesh mesh) {
        // ウィンドウをキャッシュ
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // シェーダープログラムをバインド
        shaderProgram.bind();

        // メッシュを描画します
        glBindVertexArray(mesh.getVaoId());
        glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

        // 状態を復元
        glBindVertexArray(0);

        // シェーダープログラムのバインドを解除
        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
