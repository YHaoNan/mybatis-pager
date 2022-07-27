package top.yudoge.mybatis.interceptors.pager.utils;

public class CharUtils {
    public static boolean isLetter(char c) {
        return isUpperCase(c) || isLowerCase(c);
    }
    public static boolean isUpperCase(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static boolean isLowerCase(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static char toUpperCase(char c) {
        if (isUpperCase(c)) return c;
        else return (char) (c - 32);
    }

    public static char toLowerCase(char c) {
        if (isLowerCase(c)) return c;
        else return (char) (c + 32);
    }
}
