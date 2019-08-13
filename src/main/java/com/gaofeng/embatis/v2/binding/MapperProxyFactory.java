package com.gaofeng.embatis.v2.binding;

import com.gaofeng.embatis.v2.session.DefaultSqlSession;

import java.lang.reflect.Proxy;

public class MapperProxyFactory <T>{
    private Class<T> mapperInterface;
    private Class object;

    public MapperProxyFactory(Class<T> mapperInterface, Class object) {
        this.mapperInterface = mapperInterface;
        this.object = object;
    }
    public T newInstance(DefaultSqlSession sqlSession){
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{mapperInterface},new MapperProxy(sqlSession, object));
    }

}
