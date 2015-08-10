package com.alibaba.middleware.race.rpc.test;

import java.lang.reflect.InvocationTargetException;

import com.alibaba.middleware.race.rpc.api.RpcConsumer;
import com.alibaba.middleware.race.rpc.api.RpcProvider;
import com.alibaba.middleware.race.rpc.api.impl.RpcConsumerImpl;
import com.alibaba.middleware.race.rpc.api.impl.RpcProviderImpl;

public class TestProvider {

    public static void main(String[] args) {
        RpcProvider pimpl = new RpcProviderImpl();
        pimpl.serviceInterface(RaceTestService.class);
        pimpl.impl(new RaceTestServiceImpl());
        pimpl.publish();

    }

}
