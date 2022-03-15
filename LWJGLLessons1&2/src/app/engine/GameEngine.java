package app.engine;

import org.lwjgl.Version;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.opengl.GL11.glClearColor;

public class GameEngine implements Runnable {

    private final Window window;

    private final IGameLogic gameLogic;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
    }

    @Override
    public void run() {
        try {
            System.out.println("Hello LWJGL " + Version.getVersion() + "!");

            init();
            gameLoop();

            // ウィンドウのコールバックを解放し、ウィンドウを破棄します
            window.destroy();

            // GLFWを終了し、エラーコールバックを解放します
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }

    protected void init() throws Exception {
        window.init();
    }

    protected void gameLoop() {
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
		while ( !window.windowShouldClose() ) {
            render();
		}
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
