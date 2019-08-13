package com.gaofeng.embatis.v1;

import java.lang.reflect.Proxy;
import java.util.ResourceBundle;

public class GFConfiguration {
    //向配置文件获取相关信息
    public static final ResourceBundle sqlMapping;
    static {
        sqlMapping = ResourceBundle.getBundle("v1sql");
    }

    public <T> T getMapper(Class clazz,GFSqlSession sqlSession){
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{clazz},new GFMapperProxy(sqlSession));
    }

}
