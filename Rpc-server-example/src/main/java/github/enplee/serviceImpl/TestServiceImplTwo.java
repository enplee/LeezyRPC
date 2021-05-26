package github.enplee.serviceImpl;

import github.enplee.Test;
import github.enplee.TestService;
import github.enplee.anotation.RpcService;

@RpcService(version = "1",group = "2")
public class TestServiceImplTwo implements TestService {
    @Override
    public String test(Test test) {
        return "TestServiceImplTwo" + test.getMessage()+test.getDescription();
    }
}
