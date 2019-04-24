package com.vip.wallet.utils;

import org.spongycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    private static final String KEY_AES = "AES";

    public static String encrypt(String src, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new Exception("key不满足条件");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(src.getBytes());
        return Hex.toHexString(encrypted);
    }

    public static String decrypt(String src, String key) throws Exception {
        if (key == null || key.length() != 16) {
            throw new Exception("key不满足条件");
        }
        byte[] raw = key.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, KEY_AES);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] encrypted1 = Hex.decode(src);
        byte[] original = cipher.doFinal(encrypted1);
        String originalString = new String(original);
        return originalString;
    }

}