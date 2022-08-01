package top.yudoge.mybatis.interceptors.pager.utils;

public class StringUtils {
    public static String removeSuffixIfHas(String original, String tail) {
        if (original.endsWith(tail)) {
            int lastIndex = original.lastIndexOf(tail);
            return original.substring(0, lastIndex);
        }
        return original;
    }

    public static String indexToUpperCase(String str, int index) {
        char ci = str.charAt(index);
        if (!CharUtils.isLetter(ci) || CharUtils.isUpperCase(ci)) return str;

        char[] chars = str.toCharArray();
        chars[index] = CharUtils.toUpperCase(ci);

        return new String(chars);
    }

    public static String indexToLowerCase(String str, int index) {
        char ci = str.charAt(index);
        if (!CharUtils.isLetter(ci) || CharUtils.isLowerCase(ci)) return str;

        char[] chars = str.toCharArray();
        chars[index] = CharUtils.toLowerCase(ci);

        return new String(chars);
    }
}
