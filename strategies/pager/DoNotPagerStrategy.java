package top.yudoge.mybatis.interceptors.pager.strategies.pager;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import top.yudoge.mybatis.interceptors.pager.Config;

import java.sql.SQLException;
import java.util.List;

public class DoNotPagerStrategy<E> implements PagerStrategy {


    private final Executor executor;

    public DoNotPagerStrategy(Config config, Executor e) {
        this.executor = e;
    }

    @Override
    public List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }
}
