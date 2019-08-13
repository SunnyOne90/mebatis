package com.gaofeng.embatis.v2.executor;

import com.gaofeng.embatis.v2.session.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StatementHandler {
    private ResultSetHandler resultSetHandler = new ResultSetHandler();
    public <T> T query(String sql, Object[] parameter, Class pojo) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        Object result = null;
        try{
            conn = getConnection();
            preparedStatement = conn.prepareStatement(sql);
            ParameterHandler parameterHandler = new ParameterHandler(preparedStatement);
            parameterHandler.setParameters(parameter);
            preparedStatement.execute();
            result = resultSetHandler.handle(preparedStatement.getResultSet(),pojo);
            return (T)result;
        }catch (Exception e){
            if (conn != null) {

                try {
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                conn = null;
            }
        }
        return null;
    }

    /**
     * 获取相关连接
     * @return
     */
    private Connection getConnection() {
        String driver = Configuration.properties.getString("jdbc.driver");
        String url = Configuration.properties.getString("jdbc.url");
        String username = Configuration.properties.getString("jdbc.username");
        String password = Configuration.properties.getString("jdbc.password");
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
