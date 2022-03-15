import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class App4 {
    public  static void main(String[] args) {
        List<Integer> L = d(90, 5);

        List<Map<String, Float>> V = new ArrayList<Map<String, Float>>();

        // 正五角形の頂点
        for (int i = 0; i < 5; i++) {
            V.add(calcTrigonometricCoordinates(L.get(i)));
        }

        // for(Map<String, Float> m : V) {
        //     for (Map.Entry<String, Float> entry : m.entrySet()) {
        //         System.out.println(entry.getKey() + " : " + entry.getValue());
        //     }
        // }
        // System.out.println("size : " + V.size());

        List<float[][]> F = g(V, e(V));

        for (float[][] f : F) {
            for (int i = 0; i < f.length; i++) {
                for (int j = 0; j < f[i].length; j++) {
                    System.out.println(f[i][j]);
                }
            }
        }
    }

    public static float cosf(float f) {
        return (float) Math.cos(f);
    }

    public static float sinf(double f) {
        return (float) Math.sin(f);
    }

    public static float convertDegreeToRadian(int degree) {
        return (float) (degree*Math.PI/180);
    }

    public static HashMap<String, Float> calcTrigonometricCoordinates(int degree) {
        float f = convertDegreeToRadian(degree);
        return new HashMap<>(Map.of("x", cosf(f), "y", sinf(f)));
    }

    public static List<Integer> d(int startDegree, int numberOfPolygon) {
        List<Integer> l = new ArrayList<Integer>();
        int s = 360 / numberOfPolygon;
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

    public static int factorial(int number) {
        if (number == 1) {
            return 1;
        } else {
            return number * factorial(number-1);
        }
    }

    public static int permutation(int all, int number) {
        int n = 1;

        for (int i = 0; i < number; i++) {
            n *= all-i;
        }

        return n;
    }

    public static int combination(int all, int number) {
        return permutation(all, number) / factorial(number);
    }

    public static List<List<Integer>> e(List<Map<String, Float>> V) {
        List<Integer> q = new ArrayList<Integer>();

        for (int i = 0; i < V.size(); i++) {
            q.add(i);

        }

        List<List<Integer>> q2 = new ArrayList<List<Integer>>();

        for (int i = 0; i < q.size(); i++) {
            for (int j = 0; j < q.size(); j++) {
                if (i >= j) {
                    continue;
                }

                List<Integer> q3 = new ArrayList<Integer>();
                q3.add(i);
                q3.add(j);

                q2.add(q3);
            }
        }

        List<List<Integer>> q4 = new ArrayList<List<Integer>>();

        for (int i = 0; i < q2.size(); i++) {
            if (q2.get(i).get(1) - q2.get(i).get(0) == 1) {
                continue;
            } else if (q2.get(i).get(1) - q2.get(i).get(0) == q.size()-1) {
                continue;
            }

            q4.add(q2.get(i));
            System.out.println("v" + q2.get(i).get(0) + " : v" + q2.get(i).get(1));
        }

        return q4;
    }

    public static List<float[][]> g(List<Map<String, Float>> V, List<List<Integer>> e) {
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
}
