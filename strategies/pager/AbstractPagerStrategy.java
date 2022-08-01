package top.yudoge.mybatis.interceptors.pager.strategies.pager;


import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import top.yudoge.mybatis.factories.SqlSessionFactorys;
import top.yudoge.mybatis.interceptors.pager.Config;
import top.yudoge.mybatis.interceptors.pager.Page;
import top.yudoge.mybatis.interceptors.pager.strategies.sqldialect.DialectStrategy;
import top.yudoge.mybatis.interceptors.pager.strategies.sqldialect.DialectStrategyFactory;
import top.yudoge.mybatis.interceptors.pager.utils.ExecutorUtils;
import top.yudoge.mybatis.interceptors.pager.utils.OnlyInteger;
import top.yudoge.mybatis.interceptors.pager.utils.StringUtils;

import java.sql.SQLException;
import java.util.List;

public abstract class AbstractPagerStrategy<E> implements PagerStrategy<E> {
    protected final Executor executor;
    protected final Config config;
    private static final RowBounds EMPTY_ROWBOUNDS = new RowBounds();

    public AbstractPagerStrategy(Config config, Executor e) {
        this.executor = e;
        this.config = config;
    }

    public DialectStrategy getDialectStrategy() {
        return DialectStrategyFactory.getDialectStrategy(config.getSqlDialect());
    }


    @Override
    public List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        Throwable th = null;
        List<E> queryResult = null;
        Integer totCount = null;
        try {
            // 获取原始SQL
            String originalSql = boundSql.getSql();
            // 从子类获取实际使用的RowBounds
            RowBounds realRowBounds = getPageNumberAndSize(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            // 从SQLDialect获取分页后的SQL
            String pagedQuerySql = getDialectStrategy().buildPageSql(realRowBounds.getOffset() + 1, realRowBounds.getLimit(), originalSql);
            // 构造新的BoundSql对象
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), pagedQuerySql, ms.getParameterMap().getParameterMappings(), boundSql.getParameterObject());

            // 执行查询
            queryResult = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, newBoundSql);

            // 看看是否需要执行count
            if (config.getCountEnabled()) {
                MappedStatement countMS = ExecutorUtils.getCountMappedStatement(ms, getDialectStrategy(), originalSql);
                BoundSql countBoundSql = countMS.getBoundSql(null);
                CacheKey countCacheKey = executor.createCacheKey(countMS, null, EMPTY_ROWBOUNDS, countBoundSql);
                List<OnlyInteger> counts = executor.query(countMS, null, EMPTY_ROWBOUNDS, resultHandler, countCacheKey, countBoundSql);
                if (counts.size() == 1) {
                    totCount = counts.get(0).getInteger();
                }
            }
        } catch (Throwable e) {
            th = e;
        }

        List<E> result = null;
        if (totCount != null && queryResult != null) {
            result = new Page(rowBounds.getOffset() + 1, rowBounds.getLimit(), totCount, queryResult);
        } else if (queryResult != null) {
            result = queryResult;
        }

        if (result != null) {
            onQuerySuccessed(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            onQueryDone(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
        } else if (th != null) {
            onQueryFailure(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            onQueryDone(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            // rethrow it
            if (th instanceof SQLException)
                throw (SQLException) th;
        }

        return result;
    }

    /**
     * 获取PageNumber和PageSize
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param cacheKey
     * @param boundSql
     * @return 返回一个代表PageNumber和PageSize的RowBounds对象，实现类需要将按自己方式获取到的页号和页大小封装到RowBounds对象中
     *          一些实现类的实现可能很简单，比如使用RowBounds进行分页的实现类只需要直接返回最初的RowBounds即可
     *          一些实现类的实现可能很复杂，比如使用PagerHelper进行分页的的实现类需要处理自己的逻辑并将结果转换成RowBounds
     */
    public abstract RowBounds getPageNumberAndSize(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql);

    /**
     * 当查询结束，不管成功失败都会回调，回调时，查询结果并未返回给Mybatis，回调后才返回
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param cacheKey
     * @param boundSql
     */
    public void onQueryDone(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) {

    }

    /**
     * 当查询成功但并未返回查询结果给MyBatis时回调
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param cacheKey
     * @param boundSql
     */
    public void onQuerySuccessed(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) {

    }

    /**
     * 当查询失败时回调
     * @param ms
     * @param parameter
     * @param rowBounds
     * @param resultHandler
     * @param cacheKey
     * @param boundSql
     */
    public void onQueryFailure(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) {

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
