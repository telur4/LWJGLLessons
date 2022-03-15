package chapter05.p1.main.java.org.lwjglb.engine.graph;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
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

public class Mesh {

    private final int vaoId;

    private final int posVboId;

    private final int idxVboId;

    private final int vertexCount;

    public Mesh(float[] positions, int[] indices) {
        // この後ではCベースのOpenGLライブラリと直接共有する必要がある
        // Javaオブジェクトは大抵ヒープ領域に割り当てられる
        // しかしネイティブコードとアクセスする場合にはオフヒープ領域にアクセスする必要がある
        // その為、ネイティブコード間でメモリデータを共有する唯一の方法は、Javaでメモリを直接割り当てることとなる
        FloatBuffer posBuffer = null;
        // 別のパラメータであるインデックスの配列を受け入れるようにMeshクラスを変更する必要がある
        IntBuffer indicesBuffer = null;
        try {
            // 頂点の数 : 6
            vertexCount = indices.length;

            // VAO(頂点配列オブジェクト)を作成し、それにバインドします
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // VBOは位置と頂点の番号でバッファを分ける
            // 位置のVBO
            posVboId = glGenBuffers();
            // オフヒープ領域を手動管理
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            // putを使用してデータを保存した後、
            // flipを使用してバッファの位置を0の位置にリセットする
            posBuffer.put(positions).flip();
            // バインド
            glBindBuffer(GL_ARRAY_BUFFER, posVboId);
            // データをVBOに配置する
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            // ロケーション0を有効にする
            glEnableVertexAttribArray(0);
            // データの構造を定義し、VAOの属性リストの1つに保存する
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // 番号VBO
            idxVboId = glGenBuffers();
            // オフヒープ領域を手動管理
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            // putを使用してデータを保存した後、
            // flipを使用してバッファの位置を0の位置にリセットする
            indicesBuffer.put(indices).flip();
            // バインド
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, idxVboId);
            // データをVBOに配置する
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            // VBOのバインドを解除します
            glBindBuffer(GL_ARRAY_BUFFER, 0);

            // VAOのバインドを解除します
            glBindVertexArray(0);
        } finally {
            // FloatBufferによって割り当てられたオフヒープメモリを開放する
            // Javaガベージコレクションはオフヒープ割り当てをクリーンアップしない為、手動で行う
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
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
        glDeleteBuffers(posVboId);
        glDeleteBuffers(idxVboId);

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
