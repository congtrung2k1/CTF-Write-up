// 
// Decompiled by Procyon v0.5.36
// 

package gameobject;

import java.util.Base64;
import java.security.spec.AlgorithmParameterSpec;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Encryptor
{
    public static String initVector;
    public static String key;
    
    public static String encrypt(final String key, final String initVector, final String value) {
        try {
            final IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            final SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(1, skeySpec, iv);
            final byte[] encrypted = cipher.doFinal(value.getBytes());
            return r(Base64.getEncoder().encodeToString(encrypted), 13);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String decrypt(final String key, final String initVector, final String encrypted) {
        try {
            final IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            final SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(2, skeySpec, iv);
            final byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static String r(final String input, final int r) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); ++i) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') {
                c += (char)r;
            }
            else if (c >= 'A' && c <= 'M') {
                c += (char)r;
            }
            else if (c >= 'n' && c <= 'z') {
                c -= (char)r;
            }
            else if (c >= 'N' && c <= 'Z') {
                c -= (char)r;
            }
            sb.append(c);
        }
        return sb.toString();
    }
    
    static {
        Encryptor.initVector = "JoinSunCyberSec!";
        Encryptor.key = "SunShellSunShell";
    }
}
