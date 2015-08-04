package com.alibaba.middleware.race.rpc.test;

import java.lang.reflect.InvocationTargetException;

import com.alibaba.middleware.race.rpc.api.RpcConsumer;
import com.alibaba.middleware.race.rpc.api.RpcProvider;
import com.alibaba.middleware.race.rpc.api.impl.RpcConsumerImpl;
import com.alibaba.middleware.race.rpc.api.impl.RpcProviderImpl;

public class TestProvider {

	public static void main(String[] args) {
		RpcProvider pimpl=new RpcProviderImpl();
		pimpl.serviceInterface(TestClass.class);
		pimpl.impl(new TestClassImpl());
		try {
			pimpl.publish();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
