package android.util;

// Mocked Base64 android class for testing purpose
public class Base64 {

    public static byte[] decode(String str, int flags) {
        return java.util.Base64.getDecoder().decode(str);
    }

}