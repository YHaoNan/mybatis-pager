package top.yudoge.mybatis.interceptors.pager.strategies.sqldialect;

public class MySQLDialectStrategy implements DialectStrategy {
    @Override
    public String build(int pageNum, int pageSize, String originalSql) {
        StringBuilder sb = new StringBuilder(originalSql);
        sb.append(" LIMIT ");
        sb.append(pageNum - 1);
        sb.append(", ");
        sb.append(pageSize);
        return sb.toString();
    }
}
