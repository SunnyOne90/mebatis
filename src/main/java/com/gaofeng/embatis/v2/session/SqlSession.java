package com.gaofeng.embatis.v2.session;

public interface SqlSession {
     <T> T getMapper(Class clazz);
     Object selectOne(String statement, Object[] parameter, Class pojo);
}
