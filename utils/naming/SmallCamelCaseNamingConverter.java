package top.yudoge.mybatis.interceptors.pager.utils.naming;

import top.yudoge.mybatis.interceptors.pager.utils.CharUtils;
import top.yudoge.mybatis.interceptors.pager.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SmallCamelCaseNamingConverter implements NamingConverter {

    @Override
    public List<String> toStandard(String name) {
        List<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<name.length(); i++) {
            char c = name.charAt(i);
            // 如果找到大写字符，将目前积攒的字符串添加到列表中，重新积攒字符串
            if (CharUtils.isUpperCase(c)) {
                words.add(sb.toString());
                sb = new StringBuilder();
                sb.append(CharUtils.toLowerCase(c));
                continue;
            }
            sb.append(c);
        }

        if (sb.length() != 0) {
            words.add(sb.toString());
        }
        return words;
    }

    @Override
    public String fromStandard(List<String> words) {
        if (words.size() == 0) return "";
        StringBuilder sb = new StringBuilder(words.get(0));
        words.stream()
                .skip(1)
                .forEach(word -> sb.append(StringUtils.indexToUpperCase(word, 0)));
        return sb.toString();
    }
}
