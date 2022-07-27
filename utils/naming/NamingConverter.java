package top.yudoge.mybatis.interceptors.pager.utils.naming;

import java.util.List;

/**
 * NamingConverter是命名方式转换器
 *
 * NamingConverter使用一种标准格式来保存命名中的单词，即小写列表格式
 * 比如，小驼峰命名的 instanceCreateException会被转换成 [instance, create, exception]
 *
 * 一个NamingConverter要实现两个功能：
 *   1. 将以自己的命名方式组成的字符串转换成标准格式
 *   2. 从标准格式得到以自己命名方式组成的字符串
 */
public interface NamingConverter {
    /**
     * 转换成标准格式
     * @param name 以自己的命名方式组成的字符串
     * @return 标准格式
     * @throws NamingException 如果输入的字符并不满足当前命名规范，抛出NamingException，目前的两个Converter实现都没有检测命名是否符合规范
     */
    List<String> toStandard(String name);

    /**
     * 从标准格式读取
     * @param words 标准格式
     * @return 以自己的命名方式组成的字符串
     */
    String fromStandard(List<String> words);
}
