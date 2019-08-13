package com.gaofeng.embatis.v2.plugin;

import com.gaofeng.embatis.v2.executor.Executor;

import java.util.ArrayList;
import java.util.List;

public class InterceptorChain {
    private final List<Interceptor> interceptors = new ArrayList<>();
    public boolean hasPlugin() {
        if(interceptors.size() > 0){
            return true;
        }
        return false;
    }

    public Object pluginAll(Object target) {
        for (Interceptor interceptor : interceptors) {
            target = interceptor.plugin(target);
        }
        return target;

    }

    public void addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
    }
}
