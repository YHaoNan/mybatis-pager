package top.yudoge.mybatis.interceptors.pager;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.yudoge.mybatis.interceptors.pager.strategies.PagerHelper;
import top.yudoge.mybatis.interceptors.pager.strategies.pager.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
})
public class PagerInterceptor implements Interceptor {
    private static Logger logger = LogManager.getLogger(PagerInterceptor.class);
    private Config config;

    private <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, Executor e) throws SQLException {
        logger.debug("doQuery with 4 parameters is working...redirect to 6 parameters version!");
        BoundSql boundSql = ms.getBoundSql(parameter);
        CacheKey key = e.createCacheKey(ms, parameter, rowBounds, boundSql);
        return doQuery(ms, parameter, rowBounds, resultHandler, key, boundSql, e);
    }
    private <E> List<E> doQuery(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql, Executor executor) throws SQLException {
        logger.debug("doQuery with 6 parameters is working...redirect to executor!");

        PagerStrategy<E> pagerStrategy;
        System.out.println(rowBounds.getLimit());

        if (config.getPageHelperEnabled() && PagerHelper.isPaged()) {
            pagerStrategy = new PagerHelperPagerStrategy<E>(config, executor);
        } else if (config.getMapperParamEnabled() && parameter instanceof MapperMethod.ParamMap &&
                ((MapperMethod.ParamMap) parameter).containsKey(config.getMapperParamPageNumKey()) &&
                ((MapperMethod.ParamMap) parameter).containsKey(config.getMapperParamPageSizeKey())
        ) {
            pagerStrategy = new MapperMethodParamPagerStrategy<E>(config, executor);
        } else if (rowBounds != null) {
            pagerStrategy = new RowBoundsPagerStrategy<E>(config, executor);
        } else {
            pagerStrategy = new DoNotPagerStrategy<E>(config, executor);
        }

        return pagerStrategy.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Executor executor = (Executor) invocation.getTarget();
        Object args[] = invocation.getArgs();
        List list = null;
        if (args.length == 4) {
            list = doQuery((MappedStatement) args[0], args[1], (RowBounds) args[2], (ResultHandler) args[3], executor);
        } else {
            list = doQuery((MappedStatement) args[0], args[1], (RowBounds) args[2], (ResultHandler) args[3], (CacheKey) args[4], (BoundSql) args[5], executor);
        }
        return list;
    }

    public void setProperties(Properties properties) {
        config = Config.build(properties);
    }
}
