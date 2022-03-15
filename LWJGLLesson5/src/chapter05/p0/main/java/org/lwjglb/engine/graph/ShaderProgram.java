package chapter05.p0.main.java.org.lwjglb.engine.graph;

import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    // レンダリングされる一つのオブジェクトに対して一つのシェーダープログラムIDが必要
    private final int programId;

    private int vertexShaderId;

    private int fragmentShaderId;

    public ShaderProgram() throws Exception {
        // OpenGLプログラムを作成する
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    // シェーダーごとに、新しいシェーダープログラムを作成し、そのタイプ（頂点、フラグメント）を指定します。
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    protected int createShader(String shaderCode, int shaderType) throws Exception {
        // シェーダーを作成
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        // コードをシェーダーに適用
        glShaderSource(shaderId, shaderCode);
        // シェーダーをコンパイルします。
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        // シェーダーをプログラムに接続します。
        glAttachShader(programId, shaderId);

        return shaderId;
    }

    // 全てのコードをリンクし、全てが正しく行われたことを確認する
    public void link() throws Exception {
        // プログラムをリンクします。
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        // リンクされたら、コンパイルされたシェーダーは必要ないので解放
        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        // デバッグ目的の為、本番環境では削除する
        // シェーダーが正しいかどうかを検証する
        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    // プログラムをアクティブ化してレンダリング(バインド)
    public void bind() {
        glUseProgram(programId);
    }

    // プログラムの使用を停止(バインド解除)
    public void unbind() {
        glUseProgram(0);
    }

    // プログラムの不要になった全てのリソースを解放する(ゲームループ終了時)
    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
