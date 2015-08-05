package com.alibaba.middleware.race.rpc.model;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/3/27.
 */
public class RpcResponse implements Serializable{
    static private final long serialVersionUID = -4364536436151723421L;

    private String errorMsg;

    private Object appResponse;
    
    private Map<String,Object> prop;

    public Object getAppResponse() {
        return appResponse;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isError(){
        return errorMsg == null ? false:true;
    }
    
    public Map<String, Object> getProp() {
        return prop;
    }
    
    public void setProp(Map<String, Object> prop) {
        this.prop = prop;
    }

    
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    
    public void setAppResponse(Object appResponse) {
        this.appResponse = appResponse;
    }
}
