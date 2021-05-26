package github.enplee.serviceImpl;

import github.enplee.Test;
import github.enplee.TestService;
import github.enplee.anotation.RpcService;


@RpcService(version = "1",group = "1")
public class TestServiceImpl implements TestService {
    @Override
    public String test(Test test) {
        return "TestServiceImplOne" + test.getMessage()+test.getDescription();
    }
}
