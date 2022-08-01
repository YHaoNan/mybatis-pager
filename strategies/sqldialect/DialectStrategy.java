package top.yudoge.mybatis.interceptors.pager.strategies.sqldialect;

public interface DialectStrategy {
    /**
     * 通过pageNum和pageSize重新构建用于分页的SQL语句，传入的SQL末尾没有分号，没有换行，build方法返回的末尾也不可以有分号。
     * @param pageNum 页号 从1开始
     * @param pageSize 页大小
     * @param originalSql 原始SQL
     * @return
     */
    String buildPageSql(int pageNum, int pageSize, String originalSql);

    /**
     * 通过原始SQL构建一个用于计数的SQL，传入的SQL末尾没有分号，没有换行，build方法返回的SQL末尾页不可以有分号
     * 实现类必须将SQL返回结果集中代表数量的列名指定为`Constants.COUNT_COLUMN_NAME`
     * @param originalSql 原始SQL
     * @return 用于计数的SQL，如果传入的SQL无法被转换成一个用于计数的SQL，返回null
     */
    String buildCountSql(String originalSql);
}
