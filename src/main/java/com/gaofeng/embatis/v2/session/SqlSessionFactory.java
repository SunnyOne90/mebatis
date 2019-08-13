package com.gaofeng.embatis.v2.session;


public class SqlSessionFactory {
    //用于保存解析过的配置文件
    private Configuration configuration;
    /**
     * build方法用于初始化Configuration，解析配置文件的工作在Configuration的构造函数中
     * @return
     */
    public SqlSessionFactory build(){
        configuration = new Configuration();
        return this;
    }
    public DefaultSqlSession openSqlSession(){
        return new DefaultSqlSession(configuration);
    }
}
