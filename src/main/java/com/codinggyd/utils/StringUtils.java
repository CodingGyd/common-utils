package com.codinggyd.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	private static Pattern patternMobile = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17([0167]))|(18[0-2,5-9]))\\d{8}$");
	private static Pattern patternEmail = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
	private static Pattern patternInteger = Pattern.compile("^[-+]?[\\d]*$");
	private static Pattern patternDouble = Pattern.compile("^[-+]?[.\\d]*$");
	private static Pattern patternChinese = Pattern.compile("[一-龥]");

	public StringUtils() {
	}
	/**
	 * 检查传入字符串数组元素是否均为空
	 * @param args
	 * @return 若均为空,返回true, 否则返回false
	 */
	public static boolean checkIfAllEmpty(String...args) {

		if (null == args || args.length == 0) {
			return false;
		}

		int count = 0;
		for (String arg : args) {
			if(null == arg || "".equals(arg)){
				count ++;
			}
		}

		if (count == args.length) {
			return true;
		}

		return false;
	}
	//是否邮箱
	public static boolean isEmail(CharSequence email) {
		Matcher m = patternEmail.matcher(email);
		return m.matches();
	}
	//是否手机
	public static boolean isMobile(CharSequence mobiles) {
		Matcher m = patternMobile.matcher(mobiles);
		return m.matches();
	}

	public static boolean isInteger(CharSequence str) {
		return str != null && str.toString().trim().length() != 0 ? patternInteger.matcher(str).matches() : false;
	}

	public static boolean isDouble(CharSequence str) {
		return str != null && str.toString().trim().length() != 0 ? patternDouble.matcher(str).matches() : false;
	}

	public static boolean isContainChinese(String str) {
		return str != null && str.trim().length() != 0 ? patternChinese.matcher(str).find() : false;
	}

	//将驼峰命名串转为按指定字符分割的小写串
	public static String fromCamelCase(String input, char separator) {
		int length = input.length();
		StringBuilder result = new StringBuilder(length * 2);
		int resultLength = 0;
		boolean prevTranslated = false;

		for(int i = 0; i < length; ++i) {
			char c = input.charAt(i);
			if (i > 0 || c != separator) {
				if (Character.isUpperCase(c)) {
					if (!prevTranslated && resultLength > 0 && result.charAt(resultLength - 1) != separator) {
						result.append(separator);
						++resultLength;
					}

					c = Character.toLowerCase(c);
					prevTranslated = true;
				} else {
					prevTranslated = false;
				}

				result.append(c);
				++resultLength;
			}
		}

		return resultLength > 0 ? result.toString() : input;
	}

	public static String fromCamelCase(String input) {
		return fromCamelCase(input, '_');
	}

	//将字符串转为驼峰命名法,可选首字母是否大写和分隔符
	public static String toCamelCase(String input, boolean firstCharUppercase, char separator) {
		int length = input.length();
		input = input.toLowerCase();
		StringBuilder sb = new StringBuilder(length);
		boolean upperCase = firstCharUppercase;

		for(int i = 0; i < length; ++i) {
			char ch = input.charAt(i);
			if (ch == separator) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(ch));
				upperCase = false;
			} else {
				sb.append(ch);
			}
		}

		return sb.toString();
	}

	//将字符串转为驼峰命名法，首字母小写，_分隔
	public static String toCamelCase(String input) {
		return toCamelCase(input, false, '_');
	}

	//对象转字符串，null转成""
	public static String toSafeString(Object value) {
		return value == null ? "" : value.toString();
	}

	//转小写，纯字母时较快
	public static String toLowerCase(String s) {
		if (s == null) {
			return null;
		} else {
			StringBuilder sb = null;
			int len = s.length();

			for(int i = 0; i < len; ++i) {
				char c = s.charAt(i);
				if (c > 127) {
					return s.toLowerCase();
				}

				if (c >= 'A' && c <= 'Z') {
					if (sb == null) {
						sb = new StringBuilder(s);
					}

					sb.setCharAt(i, (char)(c + 32));
				}
			}

			if (sb == null) {
				return s;
			} else {
				return sb.toString();
			}
		}
	}
	//转大写，纯字母时较快
	public static String toUpperCase(String s) {
		if (s == null) {
			return null;
		} else {
			StringBuilder sb = null;

			for(int i = 0; i < s.length(); ++i) {
				char c = s.charAt(i);
				if (c > 127) {
					return s.toUpperCase();
				}

				if (c >= 'a' && c <= 'z') {
					if (sb == null) {
						sb = new StringBuilder(s);
					}

					sb.setCharAt(i, (char)(c - 32));
				}
			}

			if (sb == null) {
				return s;
			} else {
				return sb.toString();
			}
		}
	}

	public static boolean startsWithChar(String s, char c) {
		return s.length() != 0 && s.charAt(0) == c;
	}

	public static boolean endsWithChar(String s, char c) {
		return s.length() != 0 && s.charAt(s.length() - 1) == c;
	}

	//前后增加字符
	public static String surround(String str, char c, boolean prefix, boolean suffix) {
		int len = str.length();
		int newlen = len;
		if (prefix) {
			newlen = len + 1;
		}

		if (suffix) {
			++newlen;
		}

		char[] buf = new char[newlen];
		int pos = 0;
		if (prefix) {
			buf[pos++] = c;
		}

		str.getChars(0, len, buf, pos);
		if (suffix) {
			buf[len + pos] = c;
		}

		return new String(buf);
	}

	public static String surround(String str, String fix, boolean prefix, boolean suffix) {
		int len = str.length();
		int newlen = len;
		int fixlen = fix.length();
		if (prefix) {
			newlen = len + fixlen;
		}

		if (suffix) {
			newlen += fixlen;
		}

		char[] buf = new char[newlen];
		int pos = 0;
		if (prefix) {
			fix.getChars(0, fixlen, buf, 0);
			pos += fixlen;
		}

		str.getChars(0, len, buf, pos);
		if (suffix) {
			fix.getChars(0, fixlen, buf, pos + len);
		}

		return new String(buf);
	}

	public static String surroundIfMissing(String str, char c) {
		boolean prefix = false;
		boolean suffix = false;
		if (!startsWithChar(str, c)) {
			prefix = true;
		}

		if (!endsWithChar(str, c)) {
			suffix = true;
		}

		return surround(str, c, prefix, suffix);
	}

	public static String surroundIfMissing(String str, String fix) {
		boolean prefix = false;
		boolean suffix = false;
		if (!str.startsWith(fix)) {
			prefix = true;
		}

		if (!str.endsWith(fix)) {
			suffix = true;
		}

		return surround(str, fix, prefix, suffix);
	}

	//是否含有中文字符
	public static boolean isLetter(char c) {
		int k = 128;
		return c / k == 0;
	}

	//字节长度
	public static int lengthb(final String cs) {
		if (cs == null) {
			return 0;
		} else {
			char[] c = cs.toCharArray();
			int len = 0;
			char[] var3 = c;
			int var4 = c.length;

			for(int var5 = 0; var5 < var4; ++var5) {
				char a = var3[var5];
				++len;
				if (!isLetter(a)) {
					++len;
				}
			}

			return len;
		}
	}

	public static String substringb(String cs, int lengthb) {
		if (cs == null) {
			return cs;
		} else {
			StringBuilder sb = new StringBuilder();
			char[] c = cs.toCharArray();
			int len = 0;
			char[] var5 = c;
			int var6 = c.length;

			for(int var7 = 0; var7 < var6; ++var7) {
				char ac = var5[var7];
				if (len == lengthb || len >= lengthb(cs)) {
					break;
				}

				if (!isLetter(ac)) {
					len += 2;
					if (len > lengthb) {
						break;
					}

					sb.append(ac);
				} else {
					++len;
					sb.append(ac);
				}
			}

			return sb.toString();
		}
	}

	public static String nvl(String... strs) {
		String[] var1 = strs;
		int var2 = strs.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			String str = var1[var3];
			if (isNotBlank(str)) {
				return str;
			}
		}

		return null;
	}

	public static boolean notEquals(final CharSequence cs1, final CharSequence cs2) {
		return !equals(cs1, cs2);
	}

	public static boolean noneEquals(final String cs1, final String cs2) {
		int len1 = cs1 == null ? 0 : cs1.length();
		int len2 = cs2 == null ? 0 : cs2.length();
		if (len1 == 0 && len2 == 0) {
			return true;
		} else {
			return len1 != len2 ? false : cs1.equals(cs2);
		}
	}

	public static String rightPadAll(final String str, final int size, String padStr) {
		return rightPad(str == null ? "" : str, size, padStr);
	}

	public static String joinValues(final Object... values) {
		return join(values, ",", 0, values.length);
	}

	public static String[] split(String value, String separatorChars, boolean keepNull) {
		return keepNull ? splitPreserveAllTokens(value, separatorChars) : split(value, separatorChars);
	}

	public static boolean containsInAny(String findStr, String... searchStrings) {
		String[] var2 = searchStrings;
		int var3 = searchStrings.length;

		for(int var4 = 0; var4 < var3; ++var4) {
			String searchString = var2[var4];
			if (searchString != null && searchString.contains(findStr)) {
				return true;
			}
		}

		return false;
	}

	public static String joinWithNotBlank(final String separator, final Object... objects) {
		if (objects == null) {
			throw new IllegalArgumentException("Object varargs must not be null");
		} else {
			String sanitizedSeparator = defaultString(separator);
			StringBuilder result = new StringBuilder();
			Iterator<Object> iterator = Arrays.asList(objects).iterator();

			while(iterator.hasNext()) {
				String value = Objects.toString(iterator.next(), "");
				if (!isBlank(value)) {
					result.append(value);
					if (iterator.hasNext()) {
						result.append(sanitizedSeparator);
					}
				}
			}

			return result.toString();
		}
	}
}

