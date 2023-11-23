package com.codinggyd.utils;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

/**
 * 拼音生成，基于开源的jpinyin组件
 */
public class PinyinUtils {

    public static String[] convertToPinyinArray(char c, PinyinFormat pinyinFormat) {
        return PinyinHelper.convertToPinyinArray(c, pinyinFormat);
    }
    public static boolean hasMultiPinyin(char c) {
        return PinyinHelper.hasMultiPinyin(c);
    }

    public static String[] convertToPinyinArray(char c) {
        return PinyinHelper.convertToPinyinArray(c);
    }

    public static String getShortPinyin(String str) {
        try {
            return PinyinHelper.getShortPinyin(str);
        } catch (PinyinException var2) {
            return null;
        }
    }

    public static String convertToPinyinString(String str, String separator, PinyinFormat pinyinFormat) {
        try {
            return PinyinHelper.convertToPinyinString(str, separator, pinyinFormat);
        } catch (PinyinException var4) {
            return null;
        }
    }

    public static String convertToPinyinString(String str, String separator) {
        try {
            return PinyinHelper.convertToPinyinString(str, separator);
        } catch (PinyinException var3) {
            return null;
        }
    }
}
