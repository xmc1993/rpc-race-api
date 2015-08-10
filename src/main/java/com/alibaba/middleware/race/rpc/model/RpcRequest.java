package com.alibaba.middleware.race.rpc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/5/7.
 */
public class RpcRequest{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    HashMap<String,Object> prop;
    String methodName;
    Class<?>[] paramTypes;
    Object[] params;
    
    
    public RpcRequest(HashMap<String, Object> prop, String methodName, Class<?>[] paramTypes, Object[] params){
        super();
        this.prop = prop;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.params = params;
    }
    
    
    public RpcRequest(){
        // TODO Auto-generated constructor stub
    }

    public HashMap<String, Object> getProp() {
        return prop;
    }
    
    public void setProp(HashMap<String, Object> prop) {
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

    @Override
    public String toString() {
        return "[RpcRequest="+methodName.toString()+"]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RpcRequest){
            RpcRequest re=(RpcRequest)obj;
            if(re.methodName.equals(this.methodName) && Arrays.equals(paramTypes, re.paramTypes) && Arrays.equals(this.params, re.params)){
                return true;
            }
        }
        return false;
    }

}
