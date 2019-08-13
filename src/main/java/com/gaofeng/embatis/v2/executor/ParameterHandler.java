package com.gaofeng.embatis.v2.executor;

import java.sql.PreparedStatement;

/**
 * 参数处理器
 */
public class ParameterHandler {

    private PreparedStatement pstm;
    public ParameterHandler(PreparedStatement statement) {
        this.pstm = statement;
    }

    /**
     * 从方法中获取参数遍历SQL中的？占位符
     * @param parameter
     */
    public void setParameters(Object[] parameter) {
        try {
            for (int i = 0;i<parameter.length;i++){
                int k = i+1;
                if(parameter[i] instanceof Integer){
                    pstm.setInt(k,(Integer) parameter[i]);
                }else if(parameter[i] instanceof  Long){
                    pstm.setLong(k,(Long) parameter[i]);
                }else if(parameter[i] instanceof String){
                    pstm.setString(k,(String)parameter[i]);
                }else if(parameter[i] instanceof Boolean){
                    pstm.setBoolean(k,(Boolean) parameter[i]);
                }else {
                    pstm.setString(k,String.valueOf(parameter[i]));
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
