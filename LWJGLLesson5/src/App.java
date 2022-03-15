import java.util.Map;
import java.util.HashMap;

public class App {
    public static void main(String[] args) throws Exception {
        float[][] f = {
            // {-0.951f, 1.0f, 0.309f},
            // {0.951f, 1.0f, 0.309f},

            // {-0.951f, 1.0f, 0.309f},
            // {0.587f, 1.0f, -0.809f},

            // {0.951f, 1.0f, 0.309f},
            // {-0.587f, 1.0f, -0.809f},

            // {0.0f, 1.0f, 1.0f},
            // {-0.587f, 1.0f, -0.809f},

            {1, 2, 3},
            {4, 5, 6},
            // {2, -2, 4},
            // {3, 4, -8},
        };

        calc(f);
    }

    public static void calc(float[][] l) {

        put(l);

        int column = l.length;  // 列
        int row = column+1;     // 行

        for (int i = 0; i < column; i++) {
            // ==================================================
            // ixi成分を1にする
            // ==================================================
            // 00
            float L = l[i][i];

            for (int j = i; j < row; j++) {
                // 00
                // 01
                l[i][j] *= (1/L);
                if (Math.abs(L) >= 1) {

                } else if (1 > Math.abs(L) && Math.abs(L) > 0) {

                } else if (Math.abs(L) == 0) {

                }
            }

            put(l);

            // ==================================================
            // ixi成分以外を0にする
            // ==================================================

            Map<Integer, Integer> m = new HashMap<>();

            for (int j = 0; j < column; j++) {
                if (i == j) {
                    continue;
                }

                m.put(j, i);
            }

            for (Map.Entry<Integer, Integer> entry : m.entrySet()) {

                float L2 = l[entry.getKey()][entry.getValue()];

                for (int j = 0; j < row; j++) {
                    l[entry.getKey()][j] += (l[i][j] * -L2);
                }
            }

            put(l);
        }

        System.out.println("x = " + l[0][2]);
        System.out.println("y = " + l[1][2]);
    }

    public static void put(float[][] l) {
        for (float[] fl : l) {
            for(float f : fl) {
                System.out.print(f + " ");
            }
            System.out.println();
        }
        System.out.println("=".repeat(100));
    }
}
