package top.yudoge.mybatis.interceptors.pager.strategies.pager;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.yudoge.mybatis.interceptors.pager.Config;
import top.yudoge.mybatis.interceptors.pager.strategies.PagerHelper;

import java.sql.SQLException;
import java.util.List;

public class PagerHelperPagerStrategy<E> extends AbstractPagerStrategy<E> {
    private Logger logger = LogManager.getLogger(PagerHelperPagerStrategy.class);
    public PagerHelperPagerStrategy(Config config, Executor e) {
        super(config, e);
    }

    @Override
    public List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        SQLException sqlException = null;
        try {
            RowBounds myRowBounds = PagerHelper.getPage();
            String pagedSql = getDialectStrategy().build(myRowBounds.getOffset() + 1, myRowBounds.getLimit(), getSql(boundSql));
            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), pagedSql, ms.getParameterMap().getParameterMappings(), boundSql.getParameterObject());
            return executor.query(ms, parameter, rowBounds,resultHandler, cacheKey, newBoundSql);
        } catch (SQLException e) {
            sqlException = e;
        } catch (Exception e) {
            logger.warn("PagerHelperPagerStrategy crash an exception when page", e);
        } finally {
            if (config.getPageHelperAutoClear())
                PagerHelper.clearPage();
        }
        if (sqlException != null)
            throw sqlException;
        return null;
    }
}
