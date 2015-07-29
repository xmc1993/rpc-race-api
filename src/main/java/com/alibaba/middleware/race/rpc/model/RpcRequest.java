package com.alibaba.middleware.race.rpc.model;

import com.alibaba.middleware.race.rpc.context.RpcContext;

/**
 * Created by huangsheng.hs on 2015/5/7.
 */
public class RpcRequest {
    RpcContext context;
    
    public RpcRequest(){}
    public RpcRequest context(RpcContext context){
        this.context=context;
        return this;
    }
}
