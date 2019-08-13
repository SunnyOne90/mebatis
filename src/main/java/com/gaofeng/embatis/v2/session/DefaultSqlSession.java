package com.gaofeng.embatis.v2.session;

import com.gaofeng.embatis.v2.executor.Executor;

public class DefaultSqlSession implements SqlSession{
    private Configuration configuration;
    private Executor executor;

    public DefaultSqlSession(Configuration configuration) {
        //传入初始化后的configuration
        this.configuration = configuration;
        //创建执行器executor
        this.executor = configuration.newExecutor();
    }
    //getmapper方法
    @Override
    public <T> T getMapper(Class clazz){
        //去全局的配置中寻找mapper所对应的代理工厂类
        return configuration.getMapper(clazz,this);
    }
    public Configuration getConfiguration(){
        return this.configuration;
    }
    @Override
    public Object selectOne(String statement, Object[] parameter, Class pojo){
        //通过传入的mapper类型以及相关的方法去mapper xml映射文件中寻找相关的sql
        String sql = configuration.getMappedStatement(statement);
        //通过执行器executor去调用query方法
        //当调用query时，先调用代理对象中的invoke方法
        return executor.query(sql,parameter,pojo);
    }

}
