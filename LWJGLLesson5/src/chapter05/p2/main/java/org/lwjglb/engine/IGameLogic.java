package chapter05.p2.main.java.org.lwjglb.engine;

public interface IGameLogic {

    void init() throws Exception;
    
    void input(Window window);

    void update(float interval);
    
    void render(Window window);
    
    void cleanup();
}