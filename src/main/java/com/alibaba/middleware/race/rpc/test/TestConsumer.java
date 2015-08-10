package com.alibaba.middleware.race.rpc.test;

import com.alibaba.middleware.race.rpc.api.RpcConsumer;
import com.alibaba.middleware.race.rpc.api.impl.RpcConsumerImpl;

public class TestConsumer {

	public static void main(String[] args) {
		RpcConsumer cimpl=new RpcConsumerImpl();
		cimpl.interfaceClass(RaceTestService.class).version("1.0.0.api")
        .clientTimeout(3000).hook(new RaceConsumerHook());
		RaceTestService c=(RaceTestService) cimpl.instance();
	}
	
}
