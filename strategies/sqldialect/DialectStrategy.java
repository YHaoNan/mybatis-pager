package top.yudoge.mybatis.interceptors.pager.strategies.sqldialect;

public interface DialectStrategy {
    /**
     * 通过pageNum和pageSize重新构建SQL语句，传入的SQL末尾没有分号，build方法返回的末尾也不可以有分号。
     * @param pageNum 页号 从1开始
     * @param pageSize 页大小
     * @param originalSql 原始SQL
     * @return
     */
    String build(int pageNum, int pageSize, String originalSql);
}
