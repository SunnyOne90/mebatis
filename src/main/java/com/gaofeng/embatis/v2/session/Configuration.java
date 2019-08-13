package com.gaofeng.embatis.v2.session;

import com.gaofeng.embatis.v2.annotation.Entity;
import com.gaofeng.embatis.v2.annotation.Select;
import com.gaofeng.embatis.v2.binding.MapperRegistry;
import com.gaofeng.embatis.v2.executor.CachingExecutor;
import com.gaofeng.embatis.v2.executor.Executor;
import com.gaofeng.embatis.v2.executor.SimpleExecutor;
import com.gaofeng.embatis.v2.plugin.Interceptor;
import com.gaofeng.embatis.v2.plugin.InterceptorChain;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class Configuration {
    public static final ResourceBundle sqlMappings; // 全局配置
    public static final ResourceBundle properties; // 全局配置
    private InterceptorChain interceptorChain = new InterceptorChain(); // 插件
    public static final MapperRegistry MAPPER_REGISTRY = new MapperRegistry(); // 维护接口与工厂类关系
    //缓存接口方法与映射文件中SQL的关系      
    public static final Map<String,String> mappedStatements = new HashMap<String,String>();
    private List<String> classPaths = new ArrayList<>(); // 类所有文件
    private List<Class<?>> mapperList = new ArrayList<>(); // 所有Mapper接口
    static {
        sqlMappings = ResourceBundle.getBundle("sql");
        properties = ResourceBundle.getBundle("mybatis");
    }

    public Configuration() {
        try {
            for (String key : sqlMappings.keySet()) {
                Class mapper = null;
                String statement = null;
                String pojoStr = null;
                Class pojo = null;
                // properties中的value用--隔开，第一个是SQL语句
                statement = sqlMappings.getString(key).split("--")[0];
                // properties中的value用--隔开，第二个是需要转换的POJO类型
                pojoStr = sqlMappings.getString(key).split("--")[1];
                mapper = Class.forName(key.substring(0,key.lastIndexOf(".")));
                pojo = Class.forName(pojoStr);

                MAPPER_REGISTRY.addMapper(mapper, pojo); // 接口与返回的实体类关系
                mappedStatements.put(key, statement); // 接口方法与SQL关系
            }
            String mapperPath = properties.getString("mapper.path");
            scanPackage(mapperPath);
            for (Class<?> mapper : mapperList) {
                parsingClass(mapper);
            }
            String pluginPathValue =properties.getString("plugin.path");
            String[] pluginPaths = pluginPathValue.split(",");
            if(pluginPaths != null){
                // 将插件添加到interceptorChain中
                for (String plugin : pluginPaths) {
                    Interceptor interceptor = null;
                    try {
                        interceptor = (Interceptor) Class.forName(plugin).newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    interceptorChain.addInterceptor(interceptor);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean hasStatement(String statementName){
        return mappedStatements.containsKey(statementName);
    }

    private void parsingClass(Class<?> mapper) {
        if (mapper.isAnnotationPresent(Entity.class)) {
            for (Annotation annotation : mapper.getAnnotations()) {
                if (annotation.annotationType().equals(Entity.class)) {
                    // 注册接口与实体类的映射关系
                    MAPPER_REGISTRY.addMapper(mapper, ((Entity)annotation).value());
                }
            }
        }
        Method[] methods = mapper.getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(Select.class)){
                for (Annotation dn : method.getDeclaredAnnotations()) {
                    if(dn.annotationType().equals(Select.class)){
                        String statement =method.getDeclaringClass().getName() +"."+method.getName();
                        mappedStatements.put(statement,((Select) dn).value());
                    }
                }
            }
        }
    }

    private void scanPackage(String mapperPath) {
        // TODO: 2019/8/13  
        String classPath = this.getClass().getResource("/").getPath();
        mapperPath = mapperPath.replace(".",File.separator);
        String mainPath = classPath+mapperPath;
        doPath(new File(mainPath));
        for (String className : classPaths) {
            className = className.replace(classPath.replace("/","\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");
            Class<?> clazz = null;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(clazz.isInterface()){
                mapperList.add(clazz);
            }

        }
    }

    private void doPath(File file) {
        if(file.isDirectory()){
            for (File listFile : file.listFiles()) {
                doPath(listFile);
            }
        }else{
            if(file.getName().endsWith(".class")){
                classPaths.add(file.getPath());
            }
        }

    }

    public <T> T getMapper(Class clazz, DefaultSqlSession defaultSqlSession) {
        return MAPPER_REGISTRY.getMapper(clazz,defaultSqlSession);
    }

    public String getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public Executor newExecutor() {
        Executor executor = null;
        //判断是否开启了二级缓存
        if(properties.getString("cache.enabled").equals(true)){
            executor = new CachingExecutor(new SimpleExecutor());
        }else {
            executor= new SimpleExecutor();
        }
        if (interceptorChain.hasPlugin()) {
            return (Executor)interceptorChain.pluginAll(executor);
        }
        return executor;
    }
}
