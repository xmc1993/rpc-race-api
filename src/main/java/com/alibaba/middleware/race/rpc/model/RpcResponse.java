package com.alibaba.middleware.race.rpc.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/3/27.
 */
public class RpcResponse{ 
    static private final long serialVersionUID = -4364536436151723421L;

    private String errorMsg;

    private Object appResponse;
    
    private HashMap<String,Object> prop;

    public Object getAppResponse() {
        return appResponse;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isError(){
        return errorMsg == null ? false:true;
    }
    
    public HashMap<String, Object> getProp() {
        return prop;
    }
    
    public void setProp(HashMap<String, Object> prop) {
        this.prop = prop;
    }

    
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    
    public void setAppResponse(Object appResponse) {
        this.appResponse = appResponse;
    }
    @Override
    public String toString() {
        return "[RpcResponse="+appResponse.toString()+"]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RpcResponse){
            RpcResponse re=(RpcResponse)obj;
            if(this.appResponse.equals(re.appResponse) && this.errorMsg.equals(re.errorMsg)){
                return true;
            }
        }
        return false;
    }
    
}
