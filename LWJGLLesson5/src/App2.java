public class App2 {
    public static void main(String[] args) {
        float[][] f = {
            {1, -2, 3, 5},
            {2, -1, 1, 6},
            {1, 3, -5, 2},
        };

        calc(f);
    }

    public static void calc(float[][] l) {

        // put(l);

        int column = l.length;  // 列
        int row = column+1;     // 行

        for (int i = 0; i < column; i++) {
            float L = l[i][i];

            for (int j = i; i < row; i++) {
                l[i][j] /= L;
            }

            // float L2 = 
        }

        // float L_1x1 = l[0][0];

        // for (int i = 0; i < row; i++) {
        //     l[0][i] /= L_1x1;
        // }

        // put(l);

        float L_2x1 = l[1][0];
        float L_3x1 = l[2][0];

        for (int i = 0; i < row; i++) {
            l[1][i] += (l[0][i] * -L_2x1);
            l[2][i] += (l[0][i] * -L_3x1);
        }

        // put(l);

        // float L_2x2 = l[1][1];

        // for (int i = 1; i < row; i++) {
        //     l[1][i] /= L_2x2; 
        // }

        // put(l);

        float L_1x2 = l[0][1];
        float L_3x2 = l[2][1];

        for(int i = 0; i < row; i++) {
            l[0][i] += (l[1][i] * -L_1x2);
            l[2][i] += (l[1][i] * -L_3x2);
        }

        // put(l);

        // float L_3x3 = l[2][2];

        // float p = 1;
        // if (Math.abs(L_3x3) < 1) {
        //     int pow = getPrecision(L_3x3);
        //     p = (float) (Math.pow(10, pow) / (L_3x3 * Math.pow(10, pow)));
        //     System.out.println(p);
        // }

        // for (int i = 2; i < row; i++) {
        //     l[2][i] *= p;
        // }

        // put(l);

        float L_1x3 = l[0][2];
        float L_2x3 = l[1][2];

        for (int i = 0; i < row; i++) {
            l[0][i] += (l[2][i] * -L_1x3);
            l[1][i] += (l[2][i] * -L_2x3);
        }

        // put(l);
    }

    public static int getPrecision(Float val){
        // JavaではFloat⇒int変換するやりかたは無駄な処理が多いので文字列化して数える
        String str = String.valueOf(val);

        // 文末が ".0"とか".00000"で終わってるやつは全部桁０とする
        if(str.matches("^.*\\.0+$")){
            return 0;
        }

        int index = str.indexOf(".");
        return str.substring(index + 1).length();
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
