package top.yudoge.mybatis.interceptors.pager.utils;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.*;
import top.yudoge.mybatis.interceptors.pager.Constants;
import top.yudoge.mybatis.interceptors.pager.strategies.sqldialect.DialectStrategy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ExecutorUtils {


    private static volatile ResultMap countResultMap;

    public static synchronized MappedStatement getCountMappedStatement(MappedStatement oriMs, DialectStrategy dialectStrategy, String originalSql) {
        String countId = oriMs.getId() + Constants.COUNT_MS_SUFFIX;
        MappedStatement countMS = null;

        try {
            countMS = oriMs.getConfiguration().getMappedStatement(countId, false);
        } catch (IllegalArgumentException e) {}

        if (countMS != null) return countMS;

        if (countResultMap == null) buildCountResultMap(oriMs);

        String countSql = dialectStrategy.buildCountSql(originalSql);
        SqlSource sqlSource = new SqlSourceBuilder(oriMs.getConfiguration())
                .parse(countSql, Map.class, new HashMap<>());

        countMS = new MappedStatement.Builder(oriMs.getConfiguration(), countId, sqlSource, SqlCommandType.SELECT)
                .cache(oriMs.getCache())
                .resultMaps(Arrays.asList(countResultMap))
                .build();


        oriMs.getConfiguration().addMappedStatement(countMS);

        return countMS;
    }

    private static void buildCountResultMap(MappedStatement oriMs) {
        ResultMapping mapping = new ResultMapping.Builder(oriMs.getConfiguration(), "integer", Constants.COUNT_COLUMN_NAME, Integer.class).build();
        countResultMap = new ResultMap.Builder(oriMs.getConfiguration(), Constants.COUNT_RESULT_MAP_ID, OnlyInteger.class, Arrays.asList(mapping)).build();
        oriMs.getConfiguration().addResultMap(countResultMap);
    }
}
