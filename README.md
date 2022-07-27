# Pager
一个作为学习MyBatis插件开发的分页插件项目

# 支持功能
- Mapper参数物理分页
- RowBounds物理分页
- PagerHelper物理分页

# 即将支持的功能
- count功能，自动发送一条统计表数据量的语句并返回Page对象
- Config对象初始化后返回不可变的包装对象

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