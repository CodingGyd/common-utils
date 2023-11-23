package com.codinggyd.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 加密工具，md5和des
 */
public class EncryptUtils {
    public static final String ALGORITHM = "DES";
    public static final String ENCODEING = "UTF-8";
    public static final String DESKEY = "77616e6461313139";
    public static final String CIPHER = "DES/ECB/PKCS5Padding";
    private static String password;

    public EncryptUtils() {
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        EncryptUtils.password = password;
    }

    public static String stringToHexString(String source) {
        StringBuilder hexs = new StringBuilder();

        for(int i = 0; i < source.length(); ++i) {
            int ch = source.charAt(i);
            hexs.append(Integer.toHexString(ch));
        }

        return hexs.toString();
    }

    public static String hexStringToString(String hexs) {
        if (StringUtils.isEmpty(hexs)) {
            return null;
        } else {
            String s = hexs.replace(" ", "");
            byte[] baKeyword = new byte[s.length() / 2];

            for(int i = 0; i < baKeyword.length; ++i) {
                try {
                    baKeyword[i] = (byte)(255 & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
                } catch (Exception var6) {
                    var6.printStackTrace();
                }
            }

            try {
                s = new String(baKeyword, "gbk");
            } catch (Exception var5) {
                var5.printStackTrace();
            }

            return s;
        }
    }

    public static byte[] hexStrToByte(String hexStr) {
        try {
            return Hex.decodeHex(hexStr.toCharArray());
        } catch (DecoderException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String byteToHexStr(byte[] source) {
        return new String(Hex.encodeHex(source));
    }

    public static String encode(String source, String keyStr) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            SecretKey key = new SecretKeySpec(hexStrToByte(keyStr), "DES");
            cipher.init(1, key);
            byte[] keyhex = cipher.doFinal(source.getBytes("UTF-8"));
            return byteToHexStr(keyhex);
        } catch (Exception var5) {
            throw new RuntimeException("key无效");
        }
    }

    public static String encode(String source) {
        return encode(source, "77616e6461313139");
    }

    public static String randomEncode(String source) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(1, generateKey());
            byte[] keyhex = cipher.doFinal(source.getBytes("UTF-8"));
            return byteToHexStr(keyhex);
        } catch (Exception var3) {
            throw new RuntimeException("key无效");
        }
    }

    public static String decode(String dest, String keyStr) {
        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            SecretKey key = new SecretKeySpec(hexStrToByte(keyStr), "DES");
            cipher.init(2, key);
            byte[] desthex = cipher.doFinal(hexStrToByte(dest));
            return new String(desthex, "UTF-8");
        } catch (Exception var5) {
            throw new RuntimeException("key无效", var5);
        }
    }

    public static String decode(String dest) {
        return decode(dest, "77616e6461313139");
    }

    public static SecretKey generateKey(String seed) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom(seed.getBytes("UTF-8")));
        return generator.generateKey();
    }

    public static String generateKeyStr(String seed) {
        try {
            return byteToHexStr(generateKey(seed).getEncoded());
        } catch (NoSuchAlgorithmException var2) {
            throw new RuntimeException("算法无效");
        } catch (UnsupportedEncodingException var3) {
            throw new RuntimeException("编码无效");
        }
    }

    public static SecretKey generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        KeyGenerator generator = KeyGenerator.getInstance("DES");
        generator.init(new SecureRandom());
        return generator.generateKey();
    }

    public static String generateKeyStr() {
        try {
            return byteToHexStr(generateKey().getEncoded());
        } catch (NoSuchAlgorithmException var1) {
            throw new RuntimeException("算法无效");
        } catch (UnsupportedEncodingException var2) {
            throw new RuntimeException("编码无效");
        }
    }

    public static String md5Digest(String str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException var9) {
            var9.printStackTrace();
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(password)) {
            md.update(password.getBytes());
        }

        md.update(str.getBytes());
        byte[] byteArr = md.digest();
        byte[] var5 = byteArr;
        int var6 = byteArr.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            byte aByteArr = var5[var7];
            String tmp = Integer.toHexString(aByteArr & 255);
            if (tmp.length() == 1) {
                sb.append("0");
            }

            sb.append(tmp);
        }

        return sb.toString();
    }

}
