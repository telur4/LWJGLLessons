import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.system.MemoryStack.stackPush;

public class App {

    // ウィンドウハンドル
    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // ウィンドウのコールバックを解放し、ウィンドウを破棄します
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        // GLFWを終了し、エラーコールバックを解放します
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        // エラーコールバックを設定します。デフォルトの実装では、
        // System.errにエラーメッセージが出力されます。
        GLFWErrorCallback.createPrint(System.err).set();

        // GLFWを初期化します。これを行う前は、ほとんどのGLFW関数は機能しません。
        if ( !glfwInit() ) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // GLFWを構成する
        glfwDefaultWindowHints(); // オプションで、現在のウィンドウヒントはすでにデフォルトになっています
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // 作成後、ウィンドウは非表示のままになります
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // ウィンドウはサイズ変更可能になります

        // ウィンドウを作成する
        window = glfwCreateWindow(300, 300, "Hello World!", NULL, NULL);
        if ( window == NULL ) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // キーコールバックを設定します。キーが押されたり、繰り返されたり、離されたりするたびに呼び出されます。
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            // Escapeキーを押すと、ウィンドウが閉じる
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
                glfwSetWindowShouldClose(window, true); // これはレンダリングループで検出されます
            }
        });

        // スレッドスタックを取得し、新しいフレームをプッシュします
        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // glfwCreateWindowに渡されるウィンドウサイズを取得します
            glfwGetWindowSize(window, pWidth, pHeight);

            // プライマリモニターの解像度を取得します
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // ウィンドウを中央に配置
            glfwSetWindowPos(
                window,
                (vidmode.width() - pWidth.get(0)) / 2,
                (vidmode.height() - pHeight.get(0)) / 2
            );
        } // スタックフレームが自動的にポップされます

        // OpenGLコンテキストを最新のものにする
        glfwMakeContextCurrent(window);

        // v-syncを有効にする
        glfwSwapInterval(1);

        // ウィンドウを表示する
        glfwShowWindow(window);
    }

    private void loop() {
        // この行は、LWJGLとGLFWのOpenGLコンテキスト、
        // または外部で管理されるコンテキストとの相互運用にとって重要です。
        // LWJGLは、現在のスレッドで現在使用されているコンテキストを検出し、
        // GLCapabilitiesインスタンスを作成して、
        // OpenGLバインディングを使用できるようにします。
        GL.createCapabilities();

        // クリアカラーを設定する
        glClearColor(1.0f, 0.0f, 0.0f, 0.0f);

        // ユーザーがウィンドウを閉じようとするか、ESCAPEキーを押すまで、
        // レンダリングループを実行します。
        while ( !glfwWindowShouldClose(window) ) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // フレームバッファをクリアする

            glfwSwapBuffers(window); // カラーバッファを交換します

            // ウィンドウイベントをポーリングします。
            // 上記のキーコールバックは、この呼び出し中にのみ呼び出されます。
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new App().run();
    }

}