package com.alibaba.middleware.race.rpc.context;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangsheng.hs on 2015/4/8.
 */
public class RpcContext {
    private static ThreadLocal<Map<String,Object>> propPool=new ThreadLocal<Map<String,Object>>();
//    public static Map<String,Object> props = new HashMap<String, Object>();

    public static void addProp(String key ,Object value){
        getLocalProps().put(key,value);
    }

    public static Object getProp(String key){
        return getLocalProps().get(key);
    }

    public static Map<String,Object> getProps(){
       return Collections.unmodifiableMap(getLocalProps());
    }
    
    private static Map<String,Object> getLocalProps(){
        if(propPool.get()==null){
            propPool.set(new HashMap<String, Object>());
        }
        return propPool.get();
    }
    
    public static void setLocalProps(Map<String,Object> prop){
        propPool.set(prop);
    }
    
}
