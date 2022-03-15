package chapter04.main.java.org.lwjglb.game;

import org.lwjgl.system.MemoryUtil;
import chapter04.main.java.org.lwjglb.engine.Utils;
import chapter04.main.java.org.lwjglb.engine.Window;
import chapter04.main.java.org.lwjglb.engine.graph.ShaderProgram;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Renderer {

    private int vboId;

    private int vaoId;

    private ShaderProgram shaderProgram;

    public Renderer() {
    }

    // 表示するデータをGPUの中に準備する
    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        // 頂点シェーダーとフラグメントシェーダーのコードファイルをロードします。
        shaderProgram.createVertexShader(Utils.loadResource("../../../../resources/vertex.vs"));
        shaderProgram.createFragmentShader(Utils.loadResource("../../../../resources/fragment.fs"));
        shaderProgram.link();

        // 三角形の頂点リスト
        float[] vertices = new float[] {
            //  x,     y,    z
            //  0.0f,  0.5f, 0.0f,     // v1
            // -0.5f, -0.5f, 0.0f,     // v2
            //  0.5f, -0.5f, 0.0f,     // v3
             0.0f,  0.5f, 0.0f,     // v1
            -0.433f, -0.25f, 0.0f,     // v2
             0.433f, -0.25f, 0.0f,     // v3
        };

        // この後ではCベースのOpenGLライブラリと直接共有する必要がある
        // Javaオブジェクトは大抵ヒープ領域に割り当てられる
        // しかしネイティブコードとアクセスする場合にはオフヒープ領域にアクセスする必要がある
        // その為、ネイティブコード間でメモリデータを共有する唯一の方法は、Javaでメモリを直接割り当てることとなる
        FloatBuffer verticesBuffer = null;
        try {
            // LWJGL3でのバッファ作成は二種類あり、自動管理(MemoryStack)又は手動管理(MemoryUtil)
            // この場合、データはGPUに送信されるため、自動管理も使用できるが
            // 後でそれらを使用して潜在的に大量のデータを保持するため手動で管理する必要がある
            // このtry節のfinallyでバッファを解放している理由
            verticesBuffer = MemoryUtil.memAllocFloat(vertices.length);


            // putを使用してデータを保存した後、
            // flipを使用してバッファの位置を0の位置にリセットする
            verticesBuffer.put(vertices).flip();

            // VAO(頂点配列オブジェクト)を作成し、それにバインドします
            // 三角形の頂点リストを転送
            // VAOを作成すると、識別子を返す
            // その識別子を使用して、作成時に指定した定義を使用して、識別子とそれに含まれる要素をレンダリングする
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // VBO(頂点バッファオブジェクト)を作成し、それにバインドします
            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            // データをVBOに配置する
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);
            // ロケーション0を有効にする
            glEnableVertexAttribArray(0);
            // データの構造を定義し、VAOの属性リストの1つに保存する
            // 引数
            // index      : シェーダーがこのデータを期待する場所を指定する
            // size       : 頂点属性ごとのコンポーネントの数をしる(この場合3Dなので3)
            // type       : 配列内の各コンポーネントのタイプ(この場合はfloat)
            // normalized : 値を正規化するかどうか
            // stride     : 連続する汎用頂点属性間のバイトオフセットを指定
            // offset     : バッファの最初のコンポーネントへのオフセットを指定
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // VBOのバインドを解除します
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // VAOのバインドを解除します
            glBindVertexArray(0);
        } finally {
            // FloatBufferによって割り当てられたオフヒープメモリを開放する
            // Javaガベージコレクションはオフヒープ割り当てをクリーンアップしない為、手動で行う
            if (verticesBuffer != null) {
                MemoryUtil.memFree(verticesBuffer);
            }
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    // initでデータが既にGPUにある状態なので、
    // あとはレンダリングして表示する
    public void render(Window window) {
        // ウィンドウをキャッシュ
        clear();

        if (window.isResized()) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }

        // シェーダープログラムをバインド
        shaderProgram.bind();


        // VAOにバインドする
        glBindVertexArray(vaoId);




        // 頂点を描画します
        glDrawArrays(GL_TRIANGLES, 0, 3);




        // 状態を復元
        glBindVertexArray(0);

        // シェーダープログラムのバインドを解除
        shaderProgram.unbind();
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }

        // ロケーション0を無効にする
        glDisableVertexAttribArray(0);

        // VBOを削除する
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        // VAOを削除します
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
