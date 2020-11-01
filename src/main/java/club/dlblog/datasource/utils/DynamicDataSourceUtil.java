package club.dlblog.datasource.utils;

import club.dlblog.datasource.config.DynamicDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;

/**
 * 数据源切换工具
 */
public class DynamicDataSourceUtil {

    /**
     * 动态数据源
     */
    public static DynamicDataSource dynamicDataSource = null;

    /**
     * 判断当前数据源是否存在
     *
     * @param tenantId
     * @return
     */
    public static boolean isExistDataSource(String tenantId) {
        if (dynamicDataSource != null) {
            return dynamicDataSource.getDataSourceMap().containsKey(tenantId);
        } else {
            return false;
        }
    }

    /**
     * 切换数据源
     *
     * @param key
     */
    public static void setDataSourceKey(String key) {
        if (dynamicDataSource != null) {
            dynamicDataSource.getContextHolder().set(key);
            dynamicDataSource.afterPropertiesSet();
        }
    }

    /**
     * 获取当前数据源
     *
     * @return
     */
    public static String getDataSourceKey() {
        if (dynamicDataSource != null) {
            return dynamicDataSource.getContextHolder().get();
        } else {
            return null;
        }
    }

    /**
     * 切换到默认数据源
     */
    public static void clearDataSourceKey() {
        if (dynamicDataSource != null) {
            dynamicDataSource.getContextHolder().remove();
        }
    }

    /**
     * 根据租户ID设置数据源
     *
     * @param datatBase
     * @return
     */
    public static void addDataSource(String datatBase, DataSource dataSource) {
        if (dynamicDataSource == null) {
            return;
        }
        // 没有数据源时添加数据源,有数据源直接使用
        if (!isExistDataSource(datatBase)) {
            // 新增数据源
            dynamicDataSource.getDataSourceMap().put(datatBase, dataSource);
        }
        // 切换数据源
        checkoutDataSource(datatBase);
    }

    /**
     * 切换数据源
     *
     * @param tenantId
     */
    public static void checkoutDataSource(String tenantId) {
        if (dynamicDataSource != null) {
            // 切换数据源
            setDataSourceKey(tenantId);
            dynamicDataSource.afterPropertiesSet();
        }
    }


    /**
     * 创建数据源
     * @param driverClassName
     * @param url
     * @param userName
     * @param password
     * @return
     */
    public static DataSource createDataSource(String driverClassName,String url,
                                              String userName,String password){
        return DataSourceBuilder.create().driverClassName(driverClassName)
                .url(url).username(userName).password(password).build();
    }

}
