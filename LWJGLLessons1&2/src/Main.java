import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

public class Main {

    // ? Window
    private int width;

    private int height;

    private final String title;

	// ウィンドウハンドル
	private long windowHandle;

    private boolean resized;

    private boolean vSync;

    // ? Timer
    private double lastLoopTime;

    // ? GameEngine
    public static final int TARGET_UPS = 30;

    public static final int TARGET_FPS = 75;

    // ? DummyGame
    private int direction = 0;

    private float color = 0.0f;

    public Main() {
        this.title = "GAME";
        this.width = 600;
        this.height = 480;
        this.vSync = true;
        this.resized = true;
    }

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();
	}

	private void init() {
        // ? ================================================================================
        // ? window.init
        // ? ================================================================================
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
        // TODO
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        // TODO
		// ウィンドウを作成する
		windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
		if ( windowHandle == NULL ) {
			throw new RuntimeException("Failed to create the GLFW window");
		}

        // TODO
        // Setup resize callback
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            this.width = width;
            this.height = height;
            this.setResized(true);
        });

		// キーコールバックを設定します。キーが押されたり、繰り返されたり、離されたりするたびに呼び出されます。
		glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
			// Escapeキーを押すと、ウィンドウが閉じる
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
				glfwSetWindowShouldClose(window, true); // これはレンダリングループで検出されます
			}
		});

        // プライマリモニターの解像度を取得します
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // ウィンドウを中央に配置
        glfwSetWindowPos(
                windowHandle,
                (vidmode.width() - width) / 2,
                (vidmode.height() - height) / 2
        );

		// OpenGLコンテキストを最新のものにする
		glfwMakeContextCurrent(windowHandle);

		// // v-syncを有効にする
		// glfwSwapInterval(1);

        if (isvSync()) {
            // 垂直同期を有効にした場合はフレームレートが1になる
            // Enable v-sync
            glfwSwapInterval(1);
        }

		// ウィンドウを表示する
		glfwShowWindow(windowHandle);

        // TODO
		// この行は、LWJGLとGLFWのOpenGLコンテキスト、
		// または外部で管理されるコンテキストとの相互運用にとって重要です。
		// LWJGLは、現在のスレッドで現在使用されているコンテキストを検出し、
		// GLCapabilitiesインスタンスを作成して、
		// OpenGLバインディングを使用できるようにします。
        GL.createCapabilities();

        // クリアカラーを設定する
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // ? ================================================================================
        // ? timer.init
        // ? ================================================================================
        lastLoopTime = getTime();

        // ? ================================================================================
        // ? gameLogic.init
        // ? ================================================================================
	}

	private void loop() {
        // 経過時間
        float elapsedTime;
        // 蓄蔵
        float accumulator = 0f;
        // フレームレート = リフレッシュレート?
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !this.windowShouldClose()) {
            elapsedTime = this.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();
            windowUpdate();

            if (!this.isvSync()) {
                sync();
            }
        }
	}

    // ? Window
    private void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    private boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    private boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    // public String getTitle() {
    //     return title;
    // }

    private int getWidth() {
        return width;
    }

    private int getHeight() {
        return height;
    }

    private boolean isResized() {
        return resized;
    }

    private void setResized(boolean resized) {
        this.resized = resized;
    }

    private boolean isvSync() {
        return vSync;
    }

    // public void setvSync(boolean vSync) {
    //     this.vSync = vSync;
    // }

    // ? Rename : update -> windowUpdate
    public void windowUpdate() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    // ? Timer
    private double getTime() {
        return System.nanoTime() / 1_000_000_000.0;
    }

    private float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    private double getLastLoopTime() {
        return lastLoopTime;
    }

    // ? GameEngine
    // ゲームループの反復が続く秒数を計算し、ループで費やした時間を効力してその時間待機する
    // ただし、使用可能な機関全体を一回待機する代わりに、小さな大気を実行する
    // これにより、他のタスクを実行できるようになり、前述の睡眠制度の問題を回避できる
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = this.getLastLoopTime() + loopSlot;
        while (this.getTime() < endTime) {
            try {
                // マイクロスリープ
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    // ? DummyGame
    private void input() {
        if ( this.isKeyPressed(GLFW_KEY_UP) ) {
            direction = 1;
        } else if ( this.isKeyPressed(GLFW_KEY_DOWN) ) {
            direction = -1;
        } else {
            direction = 0;
        }
    }

    private void update(float interval) {
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if ( color < 0 ) {
            color = 0.0f;
        }
    }

    private void render() {
        if (this.isResized()) {
            glViewport(0, 0, this.getWidth(), this.getHeight());
            this.setResized(false);
        }

        this.setClearColor(color, color, color, 0.0f);
        // ? Renderer
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

	public static void main(String[] args) {
		new Main().run();
	}

}