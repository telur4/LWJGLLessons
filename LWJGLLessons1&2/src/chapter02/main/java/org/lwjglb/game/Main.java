package chapter02.main.java.org.lwjglb.game;

import chapter02.main.java.org.lwjglb.engine.GameEngine;
import chapter02.main.java.org.lwjglb.engine.IGameLogic;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            // 引数
            // 1 : windowのタイトル
            // 2 : 横幅
            // 3 : 縦幅
            // 4 : 垂直同期 = ティアリングの回避(レンダリング中にビデオメモリを更新したときに生成される視覚効果・映像の乱れ)。有効にすると、画面にレンダリングされている間、GPUに画像が送信されない。
            // 5 : ゲーム本体
            GameEngine gameEng = new GameEngine("GAME", 600, 480, vSync, gameLogic);
            // Runnable
            // ゲームループを実行
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}