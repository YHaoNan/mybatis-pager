package top.yudoge.mybatis.interceptors.pager.strategies.pager;


import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import top.yudoge.mybatis.factories.SqlSessionFactorys;
import top.yudoge.mybatis.interceptors.pager.Config;
import top.yudoge.mybatis.interceptors.pager.strategies.sqldialect.DialectStrategy;
import top.yudoge.mybatis.interceptors.pager.strategies.sqldialect.DialectStrategyFactory;
import top.yudoge.mybatis.interceptors.pager.utils.StringUtils;

public abstract class AbstractPagerStrategy<E> implements PagerStrategy<E> {
    protected final Executor executor;
    protected final Config config;

    public AbstractPagerStrategy(Config config, Executor e) {
        this.executor = e;
        this.config = config;
    }

    public DialectStrategy getDialectStrategy() {
        return DialectStrategyFactory.getDialectStrategy(config.getSqlDialect());
    }

    /**
     * 通过调用BoundSql的getSql方法得到SQL语句，并且如果SQL语句末尾是`;`，移除这个`;`
     * @param boundSql
     * @return
     */
    public String getSql(BoundSql boundSql) {
        return StringUtils.removeSuffixIfHas(boundSql.getSql(), ";");
    }
}
