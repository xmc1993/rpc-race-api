package com.alibaba.middleware.race.rpc.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/5/7.
 */
public class RpcRequest implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    Map<String,Object> prop;
    String methodName;
    Class<?>[] paramTypes;
    Object[] params;
    
    
    public RpcRequest(Map<String, Object> prop, String methodName, Class<?>[] paramTypes, Object[] params){
        super();
        this.prop = prop;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.params = params;
    }

    public Map<String, Object> getProp() {
        return prop;
    }
    
    public void setProp(Map<String, Object> prop) {
        this.prop = prop;
    }

    
    public String getMethodName() {
        return methodName;
    }

    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    
    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    
    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    
    public Object[] getParams() {
        return params;
    }

    
    public void setParams(Object[] params) {
        this.params = params;
    }


}
