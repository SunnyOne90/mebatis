package com.gaofeng.embatis.v2.executor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理结果集
 */
public class ResultSetHandler {
    public <T> T handle(ResultSet resultSet, Class type) {
        //实例化要返回的对象
        Object pojo = null;
        try{
            pojo = type.newInstance();
            if (resultSet.next()){
                for (Field field :pojo.getClass().getDeclaredFields()){
                        setValue(pojo,field,resultSet);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return (T)pojo;
    }

    private void setValue(Object obj, Field field, ResultSet resultSet) {
        try {
            Method setMethod = obj.getClass().getMethod("set"+ firstWordCapital(field.getName()),field.getType());
            setMethod.invoke(obj,getResult(resultSet,field));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据反射判断类型，从ResultSet中取对应类型参数
     */
    private Object getResult(ResultSet rs, Field field) throws SQLException {
        Class type = field.getType();
        String dataName = HumpToUnderline(field.getName()); // 驼峰转下划线
        if (Integer.class == type ) {
            return rs.getInt(dataName);
        }else if (String.class == type) {
            return rs.getString(dataName);
        }else if(Long.class == type){
            return rs.getLong(dataName);
        }else if(Boolean.class == type){
            return rs.getBoolean(dataName);
        }else if(Double.class == type){
            return rs.getDouble(dataName);
        }else{
            return rs.getString(dataName);
        }
    }

    /**
     * java驼峰装数据库下滑下
     * @param name
     * @return
     */
    private String HumpToUnderline(String name) {
        StringBuffer sb = new StringBuffer(name);
        int temp = 0;
        if(!name.contains("_")){
            for (int i =0;i<name.length();i++){
                if(Character.isUpperCase(name.charAt(i))){
                    sb.insert(i+temp,"_");
                    temp+=1;
                }
            }
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 单词首字母大写
     */
    private String firstWordCapital(String word){
        String first = word.substring(0, 1);
        String tail = word.substring(1);
        return first.toUpperCase() + tail;
    }
}
