package top.yudoge.mybatis.interceptors.pager.strategies.sqldialect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DialectStrategyFactory {
    private static final Map<String, DialectStrategy> dialectMap = new HashMap<>();
    public static final String DIALECT_MYSQL = "mysql";
    static {
        dialectMap.put(DIALECT_MYSQL, new MySQLDialectStrategy());
    }

    public static DialectStrategy getDialectStrategy(String dialect) {
        return dialectMap.get(dialect);
    }

    public static boolean isDialectSupported(String dialect) {
        for (String key : dialectMap.keySet())
            if (key.equals(dialect))
                return true;
        return false;
    }

    public static Set<String> supportedDialectList() {
        return dialectMap.keySet();
    }
}
