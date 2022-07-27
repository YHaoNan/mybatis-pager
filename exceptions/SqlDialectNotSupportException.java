package top.yudoge.mybatis.interceptors.pager.exceptions;

import top.yudoge.mybatis.interceptors.pager.strategies.sqldialect.DialectStrategyFactory;

public class SqlDialectNotSupportException extends RuntimeException {
    public SqlDialectNotSupportException(String dialectName) {
        super("Unknown sql dialect : " + dialectName + ", supported sql dialect list => " + DialectStrategyFactory.supportedDialectList());
    }
}
