package app.engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
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
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.opengl.GL11.glClearColor;

public class Window {
    private final String title;

    private int width;

    private int height;

    private long windowHandle;

    private boolean vSync;

    public Window(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
    }

    public void init() {
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
		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
		if ( windowHandle == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

		// キーコールバックを設定します。キーが押されたり、繰り返されたり、離されたりするたびに呼び出されます。
		glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
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
			glfwGetWindowSize(windowHandle, pWidth, pHeight);

			// プライマリモニターの解像度を取得します
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// ウィンドウを中央に配置
			glfwSetWindowPos(
				windowHandle,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // スタックフレームが自動的にポップされます

		// OpenGLコンテキストを最新のものにする
		glfwMakeContextCurrent(windowHandle);

        if (isvSync()) {
            // v-syncを有効にする
            // 垂直同期を有効にした場合はフレームレートが1になる
            glfwSwapInterval(1);
        }

		// ウィンドウを表示する
		glfwShowWindow(windowHandle);
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

	public boolean isvSync() {
        return vSync;
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

	// !
	public void destroy() {
		glfwFreeCallbacks(windowHandle);
		glfwDestroyWindow(windowHandle);
	}
}
