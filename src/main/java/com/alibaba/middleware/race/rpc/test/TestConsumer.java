package com.alibaba.middleware.race.rpc.test;

import com.alibaba.middleware.race.rpc.api.RpcConsumer;
import com.alibaba.middleware.race.rpc.api.RpcProvider;
import com.alibaba.middleware.race.rpc.api.impl.RpcConsumerImpl;
import com.alibaba.middleware.race.rpc.api.impl.RpcProviderImpl;

public class TestConsumer {

	public static void main(String[] args) {
		RpcConsumer cimpl=new RpcConsumerImpl();
		cimpl.interfaceClass(TestClass.class);
		System.out.println("a");
		System.out.println(cimpl.instance());
	}
	
}
