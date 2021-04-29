package com.milo.libbase.utils;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 4/29/21
 */
public final class Utils {

    private static final char[] HEXCHAR = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String getMD5(String source) throws NoSuchAlgorithmException {
        byte bytes[] = digest(source.getBytes(), "MD5");
        return toHexString(bytes);
    }

    private static byte[] digest(byte srcBytes[], String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        digest.update(srcBytes);
        byte digestBytes[] = digest.digest();
        return digestBytes;
    }

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEXCHAR[(b[i] & 0xf0) >>> 4]);
            sb.append(HEXCHAR[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public static void flush(Flushable flushable) {
        try {
            if (flushable != null) {
                flushable.flush();
            }
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

}
