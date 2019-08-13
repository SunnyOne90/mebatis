package com.gaofeng.embatis.v2.binding;

import com.gaofeng.embatis.v2.session.DefaultSqlSession;

import java.util.HashMap;
import java.util.Map;

public class MapperRegistry {
    //用于保存mapper于工厂类之间的关系
    private final Map<Class<?>,MapperProxyFactory> knowMappers = new HashMap<Class<?>, MapperProxyFactory>();

    /**
     *在Configuration中解析接口上的注解时，存入接口和工厂类的映射关系
     * 此处传入pojo类型，是为了最终处理结果集的时候将结果转换为POJO类型
     * @param clazz
     * @param pojo
     * @param <T>
     */
    public <T>void addMapper(Class<T> clazz ,Class pojo){
        knowMappers.put(clazz,new MapperProxyFactory(clazz,pojo));
    }

    public <T> T getMapper(Class<?> clazz, DefaultSqlSession sqlSession){
        MapperProxyFactory proxyFactory = knowMappers.get(clazz);
        if(proxyFactory == null){
            throw  new RuntimeException("Type: " + clazz + " can not find");
        }
        return (T)proxyFactory.newInstance(sqlSession);
    }
}
