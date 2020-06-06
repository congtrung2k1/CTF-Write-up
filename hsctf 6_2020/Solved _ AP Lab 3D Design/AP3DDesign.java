import java.util.Scanner;

// 
// Decompiled by Procyon v0.5.36
// 

public class AP3DDesign
{
    public static void main(final String[] args) {
        final Scanner sc = new Scanner(System.in);
        final String inp = sc.nextLine();
        if (inp.length() != 25) {
            System.out.println("Your input is incorrect.");
            System.exit(0);
        }
        final int[][][] arr = new int[5][5][8];
        for (int i = 0; i < 25; ++i) {
            final char c = inp.charAt(i);
            for (int j = 0; j < 8; ++j) {
                arr[i / 5][i % 5][j] = numToBits(c) / (int)Math.pow(10.0, j) % 10;
            }
        }
        final String str = arrToString(shuffle2(shuffle1(arr)));
        System.out.println(str);
        if (str.equals("11010110110001101110011011001110110001100011101110101100101110011100100000111011011011000111100011111010010101100111100000111001101111101000000100101100001010011001110011001100010011101110111011001100")) {
            System.out.println("Correct. Your input is the flag.");
        }
        else {
            System.out.println("Your input is incorrect.");
        }
        System.out.println(inp);
    }
    
    public static int numToBits(final char c) {
        final byte b = (byte)c;
        final String s = Integer.toBinaryString(c);
        final int a = Integer.valueOf(s);
        return a;
    }
    
    public static int[][][] shuffle1(final int[][][] arr) {
        for (int i = 0; i < 8; ++i) {
            if (i % 2 == 1) {
                for (int j = 0; j < 5; ++j) {
                    final int k = arr[j][0][i];
                    arr[j][0][i] = arr[j][4][i];
                    arr[j][4][i] = arr[j][2][i];
                    arr[j][2][i] = arr[j][1][i];
                    arr[j][1][i] = arr[j][3][i];
                    arr[j][3][i] = k;
                }
            }
            else if (i % 2 == 0) {
                for (int j = 0; j < 5; ++j) {
                    final int k = arr[j][3][i];
                    arr[j][3][i] = arr[j][4][i];
                    arr[j][4][i] = arr[j][1][i];
                    arr[j][1][i] = arr[j][0][i];
                    arr[j][0][i] = arr[j][2][i];
                    arr[j][2][i] = k;
                }
            }
        }
        return arr;
    }
    
    public static int[][][] shuffle2(final int[][][] arr) {
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                if ((i * 5 + j * i) % 2 == 1) {
                    final int[] hi = { 1, 0, 0, 1, 0, 1, 1, 1 };
                    for (int a = 0; a < 8; ++a) {
                        arr[i][j][a] ^= hi[a];
                    }
                }
            }
        }
        return arr;
    }
    
    public static String arrToString(final int[][][] arr) {
        String s = "";
        for (int i = 0; i < 5; ++i) {
            for (int j = 0; j < 5; ++j) {
                for (int k = 0; k < 8; ++k) {
                    s = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;I)Ljava/lang/String;, s, arr[i][j][k]);
                }
            }
        }
        return s;
    }
}