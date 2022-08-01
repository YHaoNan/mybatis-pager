package top.yudoge.mybatis.interceptors.pager.strategies.sqldialect;

import top.yudoge.mybatis.interceptors.pager.Constants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MySQLDialectStrategy implements DialectStrategy {
    private final Pattern COUNT_PATTERN = Pattern.compile("(^SELECT\\s)(.*?)(\\sFROM.*?)", Pattern.CASE_INSENSITIVE);

    @Override
    public String buildPageSql(int pageNum, int pageSize, String originalSql) {
        StringBuilder sb = new StringBuilder(originalSql);
        sb.append(" LIMIT ");
        sb.append(pageNum - 1);
        sb.append(", ");
        sb.append(pageSize);
        return sb.toString();
    }

    @Override
    public String buildCountSql(String originalSql) {
        Matcher matcher = COUNT_PATTERN.matcher(originalSql);
        if (matcher.find() && matcher.groupCount() >1) {
            return matcher.replaceAll("$1count(*) as " + Constants.COUNT_COLUMN_NAME + "$3");
        }
        return null;
    }
}
