package com.codinggyd.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
/**
 * 金额和中文转换
 */
public class RmbUtils {

    private static final String[] DUNIT = new String[]{"角", "分", "厘"};
    private static final String[] IUNIT = new String[]{"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万", "拾", "佰", "仟"};
    private static final String[] NUMBERS = new String[]{"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};

    private static String getDecimal(String number) {
        int numLen = number.length();
        if (numLen > DUNIT.length) {
            numLen = DUNIT.length;
        }

        if (numLen == 0) {
            return "";
        }

        int iPos = 0;
        StringBuilder sb = new StringBuilder(numLen * 2);

        for(int last = 0; iPos < numLen; ++iPos) {
            int value = number.charAt(iPos) - 48;
            if (value != 0 || last != 0) {
                if (value > 0) {
                    if (last == 0 && iPos > 0) {
                        sb.append(NUMBERS[0]);
                    }

                    sb.append(NUMBERS[value]);
                }
                last = value;
                if (value > 0) {
                    sb.append(DUNIT[iPos]);
                }
            }
        }

        return sb.toString();
    }

    private static String getInteger(String number) {
        int numLen = number.length();
        if (numLen > IUNIT.length) {
            throw new RuntimeException("数值超出范围");
        } else if (numLen == 0) {
            return "零元";
        } else if (numLen == 1) {
            return StringUtils.join(new String[]{NUMBERS[number.charAt(0) - 48], "元"});
        } else {
            int iPos = 0;
            StringBuilder sb = new StringBuilder(numLen * 2);

            for(int last = 0; iPos < numLen; ++iPos) {
                int numpos = numLen - iPos - 1;
                int value = number.charAt(iPos) - 48;
                boolean isEdge = numpos % 4 == 0;
                if (value != 0 || last != 0 || isEdge) {
                    if (value > 0) {
                        if (last == 0 && iPos > 0) {
                            sb.append(NUMBERS[0]);
                        }

                        sb.append(NUMBERS[value]);
                    }

                    if (value > 0 || isEdge) {
                        sb.append(IUNIT[numpos]);
                    }

                    if (isEdge) {
                        last = 1;
                    } else {
                        last = value;
                    }
                }
            }

            return sb.toString();
        }
    }

    public static String toChinese(String numStr) {
        numStr = StringUtils.remove(numStr, ',');
        String prefixString = "";
        if (numStr.startsWith("-")) {
            prefixString = "负";
            numStr = numStr.substring(1);
        }

        String[] nums = StringUtils.splitPreserveAllTokens(numStr, '.');
        if (nums.length == 1) {
            return StringUtils.prependIfMissing(getInteger(nums[0]), prefixString, new CharSequence[0]);
        } else if (nums.length == 2) {
            return StringUtils.join(new String[]{prefixString, getInteger(nums[0]), getDecimal(nums[1])});
        } else {
            throw new RuntimeException("格式有误");
        }
    }


    public static String format(double money) {
        NumberFormat numFmt = NumberFormat.getInstance();
        numFmt.setMaximumFractionDigits(4);
        String numStr = numFmt.format(money);
        return numStr;
    }
    public static String toChinese(double money) {
        return toChinese(format(money));
    }

}
