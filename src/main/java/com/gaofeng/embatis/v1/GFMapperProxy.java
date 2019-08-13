package com.gaofeng.embatis.v1;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class GFMapperProxy implements InvocationHandler {
    private GFSqlSession sqlSession;

    public GFMapperProxy(GFSqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //获取接口的名称
        String mapperInterface = method.getDeclaringClass().getName();
        //获取方法的名称
        String methodName = method.getName();
        //拼接statementId
        String statementId = mapperInterface + "." + methodName;
        return sqlSession.selectOnt(statementId,args[0]);
    }
}
