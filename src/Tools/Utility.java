package Tools;

import java.util.Arrays;

/**
 * Created by Icarus on 11/16/2016.
 */
public class Utility {

    public static String replace(String p) {
        p = p.replaceAll(", ", "");
        p = p.replaceAll("]", "");
        p = p.replaceAll("\\u005B", "");
        p = p.replaceAll("null", "");
        return p;
    }

    public static int[] StringToInts(String s) {
        int[] n = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            n[i] = Integer.parseInt(s.substring(i, i + 1));
        }
        return n;
    }

    public static String DecimalismToBinary(int d) {
        int b[] = StringToInts(Integer.toBinaryString(d));
        int b1[] = new int[16];
        if (b.length > 20) {
            System.arraycopy(b, 16, b1, 0, b1.length);
        } else {
            System.arraycopy(b, 0, b1, 16 - b.length, b.length);
        }
        return replace(Arrays.toString(b1));
    }

    public static int BinaryToDecimalism(String b) {
        return Integer.parseInt(Integer.valueOf(String.valueOf(b), 2).toString());
    }
}
