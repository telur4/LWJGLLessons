package chapter05.p0.main.java.org.lwjglb.engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class Mesh {

    private final int vaoId;

    private final int vboId;

    private final int vertexCount;

    public Mesh(float[] positions) {
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
            verticesBuffer = MemoryUtil.memAllocFloat(positions.length);
            // 頂点の数は18÷3=6(頂点が重なる)
            vertexCount = positions.length / 3;
            // putを使用してデータを保存した後、
            // flipを使用してバッファの位置を0の位置にリセットする
            verticesBuffer.put(positions).flip();

            // VAO(頂点配列オブジェクト)を作成し、それにバインドします
            // 四角形の頂点リストを転送
            // VAOを作成すると、識別子を返す
            // その識別子を使用して、作成時に指定した定義を使用して、識別子とそれに含まれる要素をレンダリングする
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // VBO(頂点バッファオブジェクト)を作成し、それにバインドします
            vboId = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            // データをVBOに配置する
            glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);


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

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void cleanUp() {
        // ロケーション0を無効にする
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
