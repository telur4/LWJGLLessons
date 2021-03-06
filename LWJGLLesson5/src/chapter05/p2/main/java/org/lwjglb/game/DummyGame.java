package chapter05.p2.main.java.org.lwjglb.game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import chapter05.p2.main.java.org.lwjglb.engine.IGameLogic;
import chapter05.p2.main.java.org.lwjglb.engine.Window;
import chapter05.p2.main.java.org.lwjglb.engine.graph.Mesh;

public class DummyGame implements IGameLogic {

    private int direction = 0;

    private float color = 0.0f;

    private final Renderer renderer;
    
    private Mesh mesh;
    
    public DummyGame() {
        renderer = new Renderer();
    }

    public float cosf(float f) {
        return (float) Math.cos(f);
    }

    public float sinf(double f) {
        return (float) Math.sin(f);
    }

    public float convertDegreeToRadian(int degree) {
        return (float) (degree*Math.PI/180);
    }

    public HashMap<String, Float> calcTrigonometricCoordinates(int degree) {
        float f = convertDegreeToRadian(degree);
        return new HashMap<>(Map.of("x", cosf(f), "y", sinf(f)));
    }

    public List<Integer> d(int startDegree, int numberOfPolygon) {
        List<Integer> l = new ArrayList<Integer>();
        int s = 360 / 5;
        int n = startDegree;

        for (int i = 0; i < numberOfPolygon; i++) {
            l.add(n);
            // System.out.println(n);
            
            n += s;

            if (n >= 360) {
                n -= 360;
            }

            if (n == startDegree) {
                break;
            }
        }

        return l;
    }

    // ιδΉ
    public int factorial(int number) {
        if (number == 1) {
            return 1;
        } else {
            return number * factorial(number-1);
        }
    }

    // ι ε
    public int permutation(int all, int number) {
        int n = 1;

        for (int i = 0; i < number; i++) {
            n *= all - i;
        }

        return n;
    }

    // η΅γΏεγγ
    public int combination(int all, int number) {
        return permutation(all, number) / factorial(number);
    }

    // εγζ°γε«γζ΄ζ°γ?η΅γΏεγγ   5^2 = 25
    public List<List<Integer>> combination_r(int number) {
        List<List<Integer>> q = new ArrayList<List<Integer>>();

        // System.out.println("combination_r");
        for (int i = 0; i < number; i++) {
            for (int j = 0; j < number; j++) {
                q.add(new ArrayList<Integer>(Arrays.asList(i, j)));
                System.out.println("v" + i + " : v" + j);
            }
        }
        System.out.println("=".repeat(100));

        return q;
    }

    // εγζ°γε«γΎγͺγζ΄ζ°γ?η΅γΏεγγ   5P2 = 20
    public List<List<Integer>> combination_r2(int number) {
        List<List<Integer>> q = combination_r(number).stream()
                                        .filter(o -> o.get(0) != o.get(1))
                                        .collect(Collectors.toList());
        return q;
    }

    // θΎΊγ¨ε―Ύθ§η·   5C2 = 5P2 / 2! = 20 / 2 = 10
    public List<List<Integer>> edge_and_diagonal_r(int number) {
        List<List<Integer>> q = combination_r(number).stream()
                                        .filter(o -> o.get(0) < o.get(1))
                                        .collect(Collectors.toList());
        return q;
    }

    // ε―Ύθ§η·       n(n-3)/2 = 5*2/2 = 5
    public List<List<Integer>> diagonal_r(int number) {
        List<List<Integer>> q = edge_and_diagonal_r(number).stream()
                                        .filter(o -> o.get(1) - o.get(0) != 1)
                                        .filter(o -> o.get(1) - o.get(0) != number-1)
                                        .collect(Collectors.toList());
        return q;
    }

    public List<float[][]> g(List<Map<String, Float>> V, List<List<Integer>> e) {
        List<float[][]> F = new ArrayList<float[][]>();

        for(int i = 0; i < e.size(); i++) {
            int n1 = e.get(i).get(0);
            int n2 = e.get(i).get(1);
            float[][] f = {
                {V.get(n1).get("x"), 1.0f, V.get(n1).get("y")},
                {V.get(n2).get("x"), 1.0f, V.get(n2).get("y")},
            };
            F.add(f);
        }

        return F;
    }

    public List<float[][]> g2(List<Map<String, Float>> V, List<List<Integer>> e) {
        List<float[][]> F = new ArrayList<float[][]>();

        for(int i = 0; i < e.size(); i++) {
            int n1 = e.get(i).get(0);
            int n2 = e.get(i).get(1);
            float[][] f = {
                {-V.get(n1).get("x"), 1.0f, V.get(n1).get("y")},
                {-V.get(n2).get("x"), 1.0f, V.get(n2).get("y")},
            };
            F.add(f);
        }

        return F;
    }

    @Override
    public void init() throws Exception {
        renderer.init();

        List<Integer> L = d(90, 5);

        List<Map<String, Float>> V = new ArrayList<Map<String, Float>>();

        // ! ζ­£δΊθ§ε½’γ?ι ηΉ
        for (int i = 0; i < 5; i++) {
            V.add(calcTrigonometricCoordinates(L.get(i)));
        }

        // ? γDEBUGγ
        for(int i = 0; i < V.size(); i++) {
            System.out.print("V" + (i+1) + " : ( ");
            for (Map.Entry<String, Float> entry : V.get(i).entrySet()) {
                System.out.print(entry.getValue() + "\t");
            }
            System.out.println(")");
        }
        System.out.println("=".repeat(100));

        // ! ε―Ύθ§η·γδ½γ2γ€γ?ηΉ
        List<List<Integer>> E = diagonal_r(5);
        List<float[][]> F = g(V, E);

        // ? γDEBUGγ
        for (int i = 0; i < F.size(); i++) {
            System.out.print("V" + (E.get(i).get(0)+1) + "+V" + (E.get(i).get(1)+1) + " : ( ");
            for (int j = 0; j < F.get(i).length; j++) {
                for (int k = 0; k < F.get(i)[j].length; k++) {
                    System.out.print(F.get(i)[j][k] + "\t");
                }
            }
            System.out.println(")");
        }
        System.out.println("=".repeat(100));

        // ! ε―Ύθ§η·γ?ι’ζ°γζ±γγ
        List<Map<String, Float>> FX = new ArrayList<Map<String, Float>>();

        for (int i = 0; i < 5; i++) {
            FX.add(calc2DSimultaneousEquations(F.get(i)));
        }

        // ? γDEBUGγ
        for(int i = 0; i < FX.size(); i++) {
            System.out.print("F(x)" + (i+1) + " : V" + (E.get(i).get(0)+1) + "+V" + (E.get(i).get(1)+1) + " : ( ");
            for (Map.Entry<String, Float> entry : FX.get(i).entrySet()) {
                System.out.print(entry.getValue() + "\t");
            }
            System.out.println(")");
        }
        System.out.println("=".repeat(100));

        // ! ι’ζ°γ?δΏζ°γζ‘ε€§δΏζ°θ‘εγ?ε½’εΌγ«γγ
        List<List<Integer>> E2 = new ArrayList<List<Integer>>();
        E2.add(E.get(1));
        E2.add(E.get(0));
        E2.add(E.get(4));
        E2.add(E.get(3));
        E2.add(E.get(2));

        List<float[][]> F2 = g2(FX, E2);

        // ? γDEBUGγ
        for (int i = 0; i < F2.size(); i++) {
            System.out.print("V" + (i+6) + " : F(x)" + (E2.get(i).get(0)+1) + "+F(x)" + (E2.get(i).get(1)+1) + " : ( ");
            for (int j = 0; j < F2.get(i).length; j++) {
                for (int k = 0; k < F2.get(i)[j].length; k++) {
                    System.out.print(F2.get(i)[j][k] + "\t");
                }
            }
            System.out.println(")");
        }
        System.out.println("=".repeat(100));

        // ! δΊγ€γ?ι’ζ°γ?δΊ€ηΉγζ±γγ
        for (int i = 0; i < 5; i++) {
            V.add(calc2DSimultaneousEquations(F2.get(i)));
        }

        // ? γDEBUGγ
        for(int i = 0; i < V.size(); i++) {
            System.out.print("V" + (i+1) + " : ( ");
            for (Map.Entry<String, Float> entry : V.get(i).entrySet()) {
                System.out.print(entry.getValue() + "\t");
            }
            System.out.println(")");
        }
        System.out.println("=".repeat(100));

        // ζε½’γ?ι ηΉγͺγΉγ
        float[] positions = new float[] {
            V.get(0).get("x")/2, V.get(0).get("y")/2, 0.0f,
            V.get(1).get("x")/2, V.get(1).get("y")/2, 0.0f,
            V.get(2).get("x")/2, V.get(2).get("y")/2, 0.0f,
            V.get(3).get("x")/2, V.get(3).get("y")/2, 0.0f,
            V.get(4).get("x")/2, V.get(4).get("y")/2, 0.0f,
            V.get(5).get("x")/2, V.get(5).get("y")/2, 0.0f,
            V.get(6).get("x")/2, V.get(6).get("y")/2, 0.0f,
            V.get(7).get("x")/2, V.get(7).get("y")/2, 0.0f,
            V.get(8).get("x")/2, V.get(8).get("y")/2, 0.0f,
            V.get(9).get("x")/2, V.get(9).get("y")/2, 0.0f,
        };
        // ζε½’γ?θ²γͺγΉγ
        float[] colours = new float[] {
            // ειζ
            0.5f, 0.0f, 0.0f,       // θ΅€
            0.0f, 0.5f, 0.0f,       // η·
            0.0f, 0.0f, 0.5f,       // ι
            0.0f, 0.5f, 0.5f,       // ζ°΄θ²(γ·γ’γ³)
            0.5f, 0.5f, 0.0f,       // ι»θ²

            0.0f, 0.5f, 0.5f,       // ζ°΄θ²(γ·γ’γ³)
            0.5f, 0.5f, 0.0f,       // ι»θ²
            0.5f, 0.0f, 0.0f,       // θ΅€
            0.0f, 0.5f, 0.0f,       // η·
            0.0f, 0.0f, 0.5f,       // ι
        };
        // εθ§ε½’γ?ι ηΉγ?ηͺε·γͺγΉγ
        int[] indices = new int[] {
            0, 5, 9, 1, 6, 5,
            2, 7, 6, 3, 8, 7,
            4, 9, 8, 5, 6, 9,
            9, 6, 7, 9, 7, 8,
        };
        mesh = new Mesh(positions, colours, indices);
    }

    public HashMap<String, Float> calc2DSimultaneousEquations(float[][] l) {
        float a = l[0][0];
        float b = l[0][1];
        float c = l[0][2];
        float d = l[1][0];
        float e = l[1][1];
        float f = l[1][2];

        float x = (b*f - c*e) / (b*d - a*e);
        float y = (c - a*x) / b;

        // System.out.println("x = " + x);
        // System.out.println("y = " + y);

        return new HashMap<String, Float>(Map.of("x", x, "y", y));
    }

    @Override
    public void input(Window window) {
        if ( window.isKeyPressed(GLFW_KEY_UP) ) {           // δΈγ’γ­γΌγ­γΌγζΌγγγ¨γ
            direction = 1;                                  // δ½η½?γ1δΈγ«
        } else if ( window.isKeyPressed(GLFW_KEY_DOWN) ) {  // δΈγ’γ­γΌγ­γΌγζΌγγγ¨γ
            direction = -1;                                 // δ½η½?γ1δΈγ«
        } else {                                            // δ»γ?γ­γΌγζΌγγγε ΄ε
            direction = 0;                                  // δ½η½?γ―ε€γγͺγ
        }
    }

    @Override
    public void update(float interval) {
        // directionγ?ε€γ«γγ£γ¦θζ―θ²γε€ζ΄
        color += direction * 0.01f;
        if (color > 1) {
            color = 1.0f;
        } else if (color < 0) {
            color = 0.0f;
        }
    }

    @Override
    public void render(Window window) {
        window.setClearColor(color, color, color, 0.0f);
        renderer.render(window, mesh);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        mesh.cleanUp();
    }

}