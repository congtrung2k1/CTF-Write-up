import java.util.*;

public class solve
{
    public static void main(String args[]) {
        String flag = "r34l_g4m3rs_eXclus1v3";
        String theflag = "ÄÑÓ¿ÂÒêáøz§è§ñy÷¦";
        
        int i = 0;
        
        String[] flags = theflag.split("");
        for(i = flags.length - 1; i >= (int)((flags.length)/2); i--){
            flags[i] = Character.toString((char)((int)(flags[i].charAt(0)) - 20));
        }
        
        theflag = String.join("",flags);
                
        theflag = theflag.substring(flags.length/2);
        for(int k = 0; k < ((flags.length)/2); k++){
            theflag += flags[k];
        }
        
        String[] out = "h023456u8901234567890".split("");
        int j = 0;
        for (i = 0; i <= 6; i++, j++){
            out[i + 8] = Character.toString((char)((int)(theflag.charAt(j)) - (int)(flag.charAt(i))));
        }
        for (i = 10; i <= 14; i++, j++){
            out[i - 8] = Character.toString((char)((int)(theflag.charAt(j)) - (int)(flag.charAt(i))));
        }
        for (i = 15; i < 21; i++, j++){
            out[i] = Character.toString((char)((int)(theflag.charAt(j)) - (int)(flag.charAt(i-3))));
        }
        System.out.println(out);
    }
    
}