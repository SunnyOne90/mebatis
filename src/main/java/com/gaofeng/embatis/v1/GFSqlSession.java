package com.gaofeng.embatis.v1;

public class GFSqlSession {
    //保存着mybatis的全局的配置信息及执行器，还有操作数据的基本方法
    private GFConfiguration configuration;
    private GFExecutor executor;

    public GFSqlSession(GFConfiguration configuration, GFExecutor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }
    public <T> T selectOnt(String statementId,Object paramater){
        String sql = GFConfiguration.sqlMapping.getString(statementId);
       return executor.query(sql,paramater);
    }
    public <T> T getMapper(Class clazz){
        return configuration.getMapper(clazz,this);
    }
}
