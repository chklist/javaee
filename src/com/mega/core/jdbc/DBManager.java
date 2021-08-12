package com.mega.core.jdbc;

import com.mega.core.util.PropertyMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DBManager.class);

    private static volatile DBManager INSTANCE;

    private ConnectionPool pool;

    private int batchCount = 1000;

    private DBManager() {
        pool = new ConnectionPool();
    }

    public static DBManager getInstance() {
        if (INSTANCE == null) {
            synchronized (DBManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DBManager();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取连接
     *
     * @return Connection
     */
    public Connection getConnection() {
        return pool.getResource();
    }

    /**
     * 释放连接
     *
     * @param conn Connection
     */
    public void release(Connection conn) {
        pool.release(conn);
    }

    /*
     * 销毁池中连接
     */
    /*
    public void destroy() {
        Enumeration<Connection> keys = pool.pool.keys();
        while (keys.hasMoreElements()) {
            pool.expire(keys.nextElement());
        }
    }
    */

    /**
     * 多行查询
     *
     * @param sql       SQL语句
     * @param rowMapper 行处理
     * @param params    参数列表
     * @param <T>       实体对象
     * @return 对象集合
     * @throws Exception 异常
     */
    public <T> List<T> execMultiQuery(RowMapper<T> rowMapper, String sql, Object... params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            List<T> result = new ArrayList<>();
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T t = rowMapper.mapRow(resultSet, resultSet.getRow());
                result.add(t);
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("exec multi-row query error", e);
            throw new Exception("exec multi-row query error", e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            release(connection);
        }
    }

    /**
     * 多行查询
     *
     * @param sql       SQL语句
     * @param rowMapper 行处理
     * @param params    参数列表
     * @param <T>       实体对象
     * @return 对象集合
     * @throws Exception 异常
     */
    public <T> List<T> execMultiQueryByPage(RowMapper<T> rowMapper, String sql, int currentPage, int pageSize, Object... params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            List<T> result = new ArrayList<>();
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            int len = params.length;
            for (int i = 0; i < len; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.setInt(len + 1, (currentPage - 1) * pageSize);
            preparedStatement.setInt(len + 2, pageSize);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                T t = rowMapper.mapRow(resultSet, resultSet.getRow());
                result.add(t);
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("exec multi-row query error", e);
            throw new Exception("exec multi-row query error", e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            release(connection);
        }
    }

    /**
     * 获取记录数
     *
     * @param countSql 查询记录数语句
     * @param params   参数
     * @return 记录数
     * @throws Exception 异常
     */
    public int execQueryCount(String countSql, Object... params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(countSql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        } catch (Exception e) {
            LOGGER.error("exec count query error", e);
            throw new Exception("exec count query error", e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            release(connection);
        }
    }

    /**
     * 单行查询
     *
     * @param sql       SQL语句
     * @param rowMapper 行处理
     * @param params    参数列表
     * @param <T>       实体对象
     * @return 对象
     * @throws Exception 异常
     */
    public <T> T execSingleQuery(RowMapper<T> rowMapper, String sql, Object... params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return rowMapper.mapRow(resultSet, resultSet.getRow());
            }
            return null;
        } catch (Exception e) {
            LOGGER.error("exec single-row query error", e);
            throw new Exception("exec single-row query error", e);
        } finally {
            close(resultSet);
            close(preparedStatement);
            release(connection);
        }
    }

    /**
     * SQL增删改
     *
     * @param sql    SQL语句
     * @param params 参数
     * @throws Exception 异常
     */
    public void execUpdate(String sql, Object... params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            int rows = preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("exec update error", e);
            throw new Exception("exec update error", e);
        } finally {
            close(preparedStatement);
            release(connection);
        }
    }

    /**
     * SQL批处理
     *
     * @param <T>    实体对象
     * @param sql    SQL语句
     * @param params 参数
     * @param setter 参数设置器
     */
    public <T> void execBatchUpdate(PreparedStatementSetter<T> setter, String sql, List<T> params) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < params.size(); i++) {
                setter.setValues(preparedStatement, params.get(i));
                preparedStatement.addBatch();
                if ((i + 1) % batchCount == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            preparedStatement.executeBatch();
            preparedStatement.clearBatch();
        } catch (Exception e) {
            LOGGER.error("exec batch update error", e);
            throw new Exception("exec batch update error", e);
        } finally {
            close(preparedStatement);
            release(connection);
        }
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    private void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 静态内部类 连接池
     */
    private static class ConnectionPool extends ObjectPool<Connection> {

        private ConnectionPool() {
            try {
                Class.forName((String) PropertyMgr.getInstance().get("driver"));
                if (PropertyMgr.getInstance().get("max_connect_size") != null) {
                    maxConnectSize = Integer.parseInt((String) PropertyMgr.getInstance().get("max_connect_size"));
                }
                if (PropertyMgr.getInstance().get("max_keep_alive_time") != null) {
                    maxKeepAliveTime = Integer.parseInt((String) PropertyMgr.getInstance().get("max_keep_alive_time"));
                }
            } catch (ClassNotFoundException e) {
                LOGGER.error("create connection pool error", e);
            }
        }

        @Override
        public Connection create() {
            Connection connection = null;
            String url = (String) PropertyMgr.getInstance().get("url");
            String username = (String) PropertyMgr.getInstance().get("username");
            String password = (String) PropertyMgr.getInstance().get("password");
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                LOGGER.error("create connection error", e);
            }
            return connection;
        }

        @Override
        public boolean validate(Connection connection, long createTime) {
            return System.currentTimeMillis() - createTime <= maxKeepAliveTime;
        }

        @Override
        public void expire(Connection connection) {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

