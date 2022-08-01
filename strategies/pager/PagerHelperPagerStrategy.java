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
    public RowBounds getPageNumberAndSize(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) {
        return PagerHelper.getPage();
    }

    @Override
    public void onQueryDone(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) {
        if (config.getPageHelperAutoClear())
            PagerHelper.clearPage();
    }
}
