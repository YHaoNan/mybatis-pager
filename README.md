# Pager
一个作为学习MyBatis插件开发的分页插件项目。使用了`Strategy`、`Factory`、`Template`等设计模式

# 支持功能
- Mapper参数物理分页
- RowBounds物理分页
- PagerHelper物理分页
- 发送count，统计总条目数

# 即将支持的功能
- Config对象初始化后返回不可变的包装对象
- 更多的SQL方言
- 自动探测并选择SQL方言

# 使用
```xml
<plugins>
    <plugin interceptor="top.yudoge.mybatis.interceptors.pager.PagerInterceptor">
        <property name="sqlDialect" value="mysql"/>
    </plugin>
</plugins>
```

# 扩展
该插件在设计之初就考虑到扩展性，所以大量的用到`Strategy`设计模式。

## 分页策略
`PagerStrategy`是一个用于分页的策略，你可以实现`PagerStrategy`来构建自己的分页策略。

下面的分页策略是不分页的策略：
```java
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
```

对于复杂的分页，你也可以继承`AbstractPagerStrategy`，它提供了一些基本功能，如发送`count`语句，包装结果集到`Page`对象，还提供了查询的生命周期方法：`onQuerySuccessed`、`onQueryFailure`、`onQueryDone`，一些查询策略（如使用ThreaLocal的查询策略）可能需要监听查询的生命周期以做一些清理操作。

> 目前，考虑到分页策略不会经常变更，所以不支持动态添加分页策略，内置的四种分页策略硬编码到`PagerInterceptor`中。如果考虑组织职责链模式。

## 方言策略
默认情况下，该插件只支持MySQL的分页方言，因为我没学过其它数据库。

不过想要支持某种特定SQL方言也很简单，只需要实现`DialectStrategy`，实现两个方法，通过原SQL构建分页SQL和CountSQL即可：

```java
public interface DialectStrategy {
    /**
     * 通过pageNum和pageSize重新构建用于分页的SQL语句，传入的SQL末尾没有分号，没有换行，build方法返回的末尾也不可以有分号。
     * @param pageNum 页号 从1开始
     * @param pageSize 页大小
     * @param originalSql 原始SQL
     * @return
     */
    String buildPageSql(int pageNum, int pageSize, String originalSql);

    /**
     * 通过原始SQL构建一个用于计数的SQL，传入的SQL末尾没有分号，没有换行，build方法返回的SQL末尾页不可以有分号
     * 实现类必须将SQL返回结果集中代表数量的列名指定为`Constants.COUNT_COLUMN_NAME`
     * @param originalSql 原始SQL
     * @return 用于计数的SQL，如果传入的SQL无法被转换成一个用于计数的SQL，返回null
     */
    String buildCountSql(String originalSql);
}
```

> 考虑到SQL方言策略是随着插件一起打包的，所以方言策略也不支持动态添加，不过可以很方便的去到`DialectStrategyFactory`中添加方言策略：
> 
> ```java
> static {
>   dialectMap.put(DIALECT_MYSQL, new MySQLDialectStrategy());
> }
> ```


# PagerInterceptor分页插件优先级定义
在我们的插件中也支持`PageHelper`中的三种形式的用法：

1. 使用Mapper参数
2. 使用线程私有的PageHelper
3. 使用RowBounds

在我们的插件中，它们默认都是开启的，并且它们之间的优先级就如上面的列表顺序所示。

# property列表
```java
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
```