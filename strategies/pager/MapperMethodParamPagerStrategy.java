package top.yudoge.mybatis.interceptors.pager.strategies.pager;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import top.yudoge.mybatis.interceptors.pager.Config;

import java.sql.SQLException;
import java.util.List;

public class MapperMethodParamPagerStrategy<E> extends AbstractPagerStrategy<E> {
    public MapperMethodParamPagerStrategy(Config config, Executor e) {
        super(config, e);
    }

    @Override
    public List<E> query(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) throws SQLException {
        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;
        int pageNum = 0;
        int pageSize = Integer.MAX_VALUE;
        try {
            pageNum = (int) paramMap.get(config.getMapperParamPageNumKey());
            pageSize = (int) paramMap.get(config.getMapperParamPageSizeKey());
        } catch (Exception e) {}

        String pagedSql = getDialectStrategy().build(pageNum, pageSize, getSql(boundSql));
        BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), pagedSql, ms.getParameterMap().getParameterMappings(), boundSql.getParameterObject());
        return executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, newBoundSql);
    }
}
