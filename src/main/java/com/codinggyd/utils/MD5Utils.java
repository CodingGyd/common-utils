package com.codinggyd.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;

public class MD5Utils {
    private static final String DEFAULT_ENCODING = "UTF-8";

    private MD5Utils() {
    }

    public static String encode(String originString) {
        String result = null;
        if (!StringUtils.isEmpty(originString)) {
            try {
                result = DigestUtils.md5Hex(originString.getBytes(DEFAULT_ENCODING));
            } catch (UnsupportedEncodingException e) {
                result = DigestUtils.md5Hex(originString.getBytes());
            }
        }

        return result;
    }
}
