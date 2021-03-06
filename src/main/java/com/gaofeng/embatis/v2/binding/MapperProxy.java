package com.gaofeng.embatis.v2.binding;

import com.gaofeng.embatis.v2.session.DefaultSqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MapperProxy implements InvocationHandler {
    private DefaultSqlSession sqlSession;
    private Class object;
    public MapperProxy(DefaultSqlSession sqlSession, Class object) {
        this.object = object;
        this.sqlSession = sqlSession;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String mapperInterface = method.getDeclaringClass().getName();
        String methodName = method.getName();
        String statementId = mapperInterface + "." +methodName;
        if(sqlSession.getConfiguration().hasStatement(statementId)) {
            return sqlSession.selectOne(statementId, args, object);
        }
       return method.invoke(proxy,args);
    }
}
