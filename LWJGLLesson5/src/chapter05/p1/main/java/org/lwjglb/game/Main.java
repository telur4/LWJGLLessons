package chapter05.p1.main.java.org.lwjglb.game;

import chapter05.p1.main.java.org.lwjglb.engine.GameEngine;
import chapter05.p1.main.java.org.lwjglb.engine.IGameLogic;

public class Main {

    public static void main(String[] args) {
        try {
            boolean vSync = true;
            IGameLogic gameLogic = new DummyGame();
            GameEngine gameEng = new GameEngine("GAME", 600, 600, vSync, gameLogic);
            gameEng.run();
        } catch (Exception excp) {
            excp.printStackTrace();
            System.exit(-1);
        }
    }
}
