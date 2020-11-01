package club.dlblog.datasource.config;

import club.dlblog.datasource.utils.DynamicDataSourceUtil;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源配置
 */

public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 数据源存储用map
     */
    private  final Map<Object,Object> dataSourceMap = new ConcurrentHashMap<>();


    /**
     * 当前线程数据源key
     */
    private final ThreadLocal<String> contextHolder = new ThreadLocal<String>();


    @Override
    protected Object determineCurrentLookupKey() {
        //弹出当前数据源key
        return contextHolder.get();
    }

    /**
     * 动态数据源构造方法
     * @param defaultDataSource
     */
    public DynamicDataSource(DataSource defaultDataSource) {
        //存入主数据源
        dataSourceMap.put("master",defaultDataSource);
        //设定目标数据源map
        setTargetDataSources(dataSourceMap);
        //设定默认数据源
        setDefaultTargetDataSource(defaultDataSource);
        DynamicDataSourceUtil.dynamicDataSource =this;
    }


    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    public ThreadLocal<String> getContextHolder() {
        return contextHolder;
    }
}
