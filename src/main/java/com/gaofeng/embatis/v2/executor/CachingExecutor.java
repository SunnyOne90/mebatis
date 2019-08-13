package com.gaofeng.embatis.v2.executor;

import com.gaofeng.embatis.v2.cache.CacheKey;

import java.util.HashMap;
import java.util.Map;

public class CachingExecutor implements Executor {

    private Executor dalegate;
    private static final Map<Integer, Object> cache = new HashMap<Integer, Object>();

    public CachingExecutor(Executor delegate) {
        this.dalegate = dalegate;
    }

    public <T> T query(String sql, Object[] parameter, Class pojo) {
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(sql);
        cacheKey.update(joinStr(parameter));
        if(cache.containsKey(cacheKey)){
            System.out.println("命中缓存");
            return (T)cache.get(cacheKey.getCode());
        }else{
            Object obj = dalegate.query(sql,parameter,pojo);
            cache.put(cacheKey.getCode() ,obj);
            return (T)obj;
        }
    }

    private Object joinStr(Object[] parameter) {
        StringBuffer sb = new StringBuffer();
        if(null != parameter && parameter.length>0){
            for (int i=0;i<parameter.length;i++){
                sb.append(parameter[i] +",");
            }
        }
        int len = sb.length();
        if(len > 0){
            sb.deleteCharAt(len -1);
        }
        return sb.toString();
    }
}
