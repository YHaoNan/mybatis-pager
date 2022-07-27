package top.yudoge.mybatis.interceptors.pager;

import top.yudoge.mybatis.interceptors.pager.exceptions.SqlDialectNotSupportException;
import top.yudoge.mybatis.interceptors.pager.strategies.sqldialect.DialectStrategyFactory;
import top.yudoge.mybatis.interceptors.pager.utils.PropertiesUtils;

import java.util.Properties;

public final class Config {

    private Config() {}

    /**
     * 使用Mapper参数分页开关
     */
    private Boolean mapperParamEnabled = true;
    /**
     * Mapper参数中哪一个代表页数
     */
    private String mapperParamPageNumKey = "pageNum";
    /**
     * Mapper参数中哪一个代表页大小
     */
    private String mapperParamPageSizeKey = "pageSize";
    /**
     * 是否启用PageHelper
     */
    private Boolean pageHelperEnabled = true;
    /**
     * 自动清理PageHelper残留的线程私有对象吗
     */
    private Boolean pageHelperAutoClear = true;

    /**
     * SQL方言
     */
    private String sqlDialect = DialectStrategyFactory.DIALECT_MYSQL;

    /**
     * 是否开启计数，如果开启，则将在发送实际的SQL语句之前发送一个计数语句
     * 并且返回的List变成Page
     */
    private boolean countEnabled = true;

    public void setCountEnabled(boolean countEnabled) {
        this.countEnabled = countEnabled;
    }

    public boolean getCountEnabled() {
        return countEnabled;
    }

    public Boolean getMapperParamEnabled() {
        return mapperParamEnabled;
    }

    public void setMapperParamEnabled(Boolean mapperParamEnabled) {
        this.mapperParamEnabled = mapperParamEnabled;
    }

    public String getMapperParamPageNumKey() {
        return mapperParamPageNumKey;
    }

    public void setMapperParamPageNumKey(String mapperParamPageNumKey) {
        this.mapperParamPageNumKey = mapperParamPageNumKey;
    }

    public String getMapperParamPageSizeKey() {
        return mapperParamPageSizeKey;
    }

    public void setMapperParamPageSizeKey(String mapperParamPageSizeKey) {
        this.mapperParamPageSizeKey = mapperParamPageSizeKey;
    }

    public Boolean getPageHelperEnabled() {
        return pageHelperEnabled;
    }

    public void setPageHelperEnabled(Boolean pageHelperEnabled) {
        this.pageHelperEnabled = pageHelperEnabled;
    }

    public Boolean getPageHelperAutoClear() {
        return pageHelperAutoClear;
    }

    public void setPageHelperAutoClear(Boolean pageHelperAutoClear) {
        this.pageHelperAutoClear = pageHelperAutoClear;
    }

    public void setSqlDialect(String sqlDialect) {
        this.sqlDialect = sqlDialect;
    }

    public String getSqlDialect() {
        return sqlDialect;
    }

    @Override
    public String toString() {
        return "Config{" +
                "mapperParamEnabled=" + mapperParamEnabled +
                ", mapperParamPageNumKey='" + mapperParamPageNumKey + '\'' +
                ", mapperParamPageSizeKey='" + mapperParamPageSizeKey + '\'' +
                ", pageHelperEnabled=" + pageHelperEnabled +
                ", pageHelperAutoClear=" + pageHelperAutoClear +
                ", sqlDialect=" + sqlDialect +
                ", countEnabled=" + countEnabled +
                '}';
    }

    public static Config build(Properties properties) {
        Config config = new Config();
        PropertiesUtils.readToObject(properties, () -> config);
        if (!DialectStrategyFactory.isDialectSupported(config.getSqlDialect()))
            throw new SqlDialectNotSupportException(config.sqlDialect);
        return config;
    }
}
