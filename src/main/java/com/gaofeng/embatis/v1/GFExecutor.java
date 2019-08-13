package com.gaofeng.embatis.v1;

import com.gaofeng.embatis.v1.mapper.Blog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GFExecutor {
    public <T> T query(String sql,Object paramater){
        Connection conn = null;
        Statement stmt = null;
        Blog blog = new Blog();
        try{
            //注册驱动
            Class.forName("com.mysql.jdbc.Driver");
            //建立连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/gp-mybatis", "root", "root");
            //创建语句集
            stmt = conn.createStatement();
            //执行查询
            ResultSet rs = stmt.executeQuery(String.format(sql,paramater));
            //获取结果集
            while (rs.next()){
                Integer bid = rs.getInt("bid");
                String name = rs.getString("name");
                Integer authorId = rs.getInt("author_id");
                blog.setBid(bid);
                blog.setName(name);
                blog.setAuthorId(authorId);
            }
            //关闭结果集
            rs.close();
            //关闭语句集
            stmt.close();
            //关闭连接
            conn.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                //关闭结果集
                if (stmt != null) {
                    stmt.close();
                }
                //关闭连接
                if (conn != null) {
                    conn.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        return (T)blog;
    }
}
