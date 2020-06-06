import java.util.Scanner;

// 
// Decompiled by Procyon v0.5.36
// 

public class APStatistics
{
    public static void main(final String[] args) {
        final Scanner sc = new Scanner(System.in);
        System.out.println("What is the flag?");
        final String guess = sc.nextLine();
        final String distorted = toString(swapArray(toNumbers(guess)));
        if (distorted.equals("qtqnhuyj{fjw{rwhswzppfnfrz|qndfktceyba")) {
            System.out.println(invokedynamic(makeConcatWithConstants:(Ljava/lang/String;)Ljava/lang/String;, guess));
        }
        else {
            System.out.println("You may have still entered the correct flag. Keep Trying.");
        }
    }
    
    private static int[] toNumbers(final String guess) {
        final int[] arr = new int[guess.length()];
        arr[0] = 97 + (int)(Math.random() * 26.0);
        for (int i = 1; i < guess.length(); ++i) {
            if (arr[i - 1] % 2 == 0) {
                arr[i] = guess.charAt(i) + (arr[i - 1] - 97);
            }
            else {
                arr[i] = guess.charAt(i) - (arr[i - 1] - 97);
            }
            arr[i] = (arr[i] - 97 + 29) % 29 + 97;
        }
        return swapArray(arr);
    }
    
    private static int[] swapArray(final int[] arr) {
        for (int i = 1; i < arr.length; ++i) {
            if (arr[i - 1] <= arr[i]) {
                flip(arr, i, i - 1);
            }
        }
        return arr;
    }
    
    private static String toString(final int[] arr) {
        String ans = "";
        for (final int x : arr) {
            ans = invokedynamic(makeConcatWithConstants:(Ljava/lang/String;C)Ljava/lang/String;, ans, (char)x);
        }
        return ans;
    }
    
    public static void flip(final int[] arr, final int a, final int b) {
        final int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }
}
