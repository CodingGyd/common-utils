package com.codinggyd.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 去除HTML的标签信息
 */
public class HtmlUtils {

    public static String html2Text(final String html) {
        if (html == null) {
            return null;
        }

        String htmlStr = html;
        String result = "";

        try {
            String regexHtml = "<[^>]+>";
            String regexScript = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            String patternStr = "\\s+";
            String regexStyle = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";

            Pattern patternScript = Pattern.compile(regexScript, 2);
            Matcher matcherScript = patternScript.matcher(htmlStr);
            htmlStr = matcherScript.replaceAll("");
            Pattern patternStyle = Pattern.compile(regexStyle, 2);
            Matcher matcherStyle = patternStyle.matcher(htmlStr);
            htmlStr = matcherStyle.replaceAll("");
            Pattern patterHtml = Pattern.compile(regexHtml, 2);
            Matcher matcherHtml = patterHtml.matcher(htmlStr);
            htmlStr = matcherHtml.replaceAll("");
            Pattern patternBa = Pattern.compile(patternStr, 2);
            Matcher matcherBa = patternBa.matcher(htmlStr);
            htmlStr = matcherBa.replaceAll("");
            result = htmlStr;
        } catch (Exception var15) {
            System.err.println("html2Text-error: " + var15.getMessage());
        }

        return result;
    }
}
