package chapter02.main.java.org.lwjglb.engine;

public class GameEngine implements Runnable {

    public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 30;

    private final Window window;

    private final Timer timer;

    private final IGameLogic gameLogic;

    public GameEngine(String windowTitle, int width, int height, boolean vSync, IGameLogic gameLogic) throws Exception {
        // ウィンドウの用意
        window = new Window(windowTitle, width, height, vSync);
        this.gameLogic = gameLogic;
        // タイマーの用意
        timer = new Timer();
    }

    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } catch (Exception excp) {
            excp.printStackTrace();
        }
    }

    // ================================================================================
    // ゲームの初期化
    // ================================================================================
    protected void init() throws Exception {
        window.init();      // ウィンドウの初期化
        timer.init();       // タイマーの初期化
        gameLogic.init();   // ゲーム本体の初期化(内部でレンダリングの初期化も行う)
    }

    // ================================================================================
    // ゲームループ
    // ================================================================================
    protected void gameLoop() {
        float elapsedTime;                  // 経過時間
        float accumulator = 0f;             // 蓄蔵
        float interval = 1f / TARGET_UPS;   // フレームレート = リフレッシュレート?

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    // ゲームループの反復が続く秒数を計算し、ループで費やした時間を効力してその時間待機する
    // ただし、使用可能な機関全体を一回待機する代わりに、小さな大気を実行する
    // これにより、他のタスクを実行できるようになり、前述の睡眠制度の問題を回避できる
    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                // マイクロスリープ
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            }
        }
    }

    // !
    protected void input() {
        gameLogic.input(window);
    }

    // !
    protected void update(float interval) {
        gameLogic.update(interval);
    }

    // !
    protected void render() {
        gameLogic.render(window);
        window.update();
    }
}
