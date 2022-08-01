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
    public RowBounds getPageNumberAndSize(MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, CacheKey cacheKey, BoundSql boundSql) {
        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) parameter;

        int pageNum = 0;
        int pageSize = Integer.MAX_VALUE;

        try {
            pageNum = (int) paramMap.get(config.getMapperParamPageNumKey());
            pageSize = (int) paramMap.get(config.getMapperParamPageSizeKey());
        } catch (Exception e) {}
        return new RowBounds(pageNum - 1, pageSize);
    }

}
