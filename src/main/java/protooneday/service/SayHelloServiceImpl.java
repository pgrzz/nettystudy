package protooneday.service;

import protooneday.server.annotation.RpcService;

/**
 * Created by lenovo on 2016/5/30.
 */
@RpcService(SayHelloService.class)
public class SayHelloServiceImpl implements  SayHelloService {


    @Override
    public String sayHello(String hello) {
        System.out.println(hello);
        return hello.equals("hello")?"hello":"godbye";
    }
}
