package com.qg.bank.util;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static DruidDataSource dataSource = new DruidDataSource();
    private static Properties properties = new Properties();

    static {
        try (InputStream input = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            // 加载配置文件
            properties.load(input);
            
            // 配置Druid连接池
            dataSource.setDriverClassName(properties.getProperty("driverClassName"));
            dataSource.setUrl(properties.getProperty("jdbcUrl"));
            dataSource.setUsername(properties.getProperty("username"));
            dataSource.setPassword(properties.getProperty("password"));
            
            // 连接池基本配置
            dataSource.setInitialSize(5);           // 初始连接数
            dataSource.setMinIdle(5);               // 最小空闲连接数
            dataSource.setMaxActive(20);            // 最大活跃连接数
            dataSource.setMaxWait(60000);           // 获取连接的最大等待时间（毫秒）
            
            // 连接检测配置
            dataSource.setTimeBetweenEvictionRunsMillis(60000);  // 检测间隔（毫秒）
            dataSource.setMinEvictableIdleTimeMillis(300000);    // 连接最小生存时间（毫秒）
            dataSource.setValidationQuery("SELECT 1");           // 检测连接是否有效的SQL
            dataSource.setTestWhileIdle(true);                   // 空闲时检测
            dataSource.setTestOnBorrow(false);                   // 获取连接时不检测
            dataSource.setTestOnReturn(false);                   // 归还连接时不检测
            
            // 其他配置
            dataSource.setPoolPreparedStatements(true);          // 开启PreparedStatement缓存
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();  // 实际是归还给连接池，不是真正关闭
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
