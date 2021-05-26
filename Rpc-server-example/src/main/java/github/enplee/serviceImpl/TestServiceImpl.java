package github.enplee.serviceImpl;

import github.enplee.Test;
import github.enplee.TestService;
import github.enplee.spring.anotation.RpcService;
import org.springframework.stereotype.Component;


@RpcService
@Component
public class TestServiceImpl implements TestService {
    @Override
    public String test(Test test) {
        return test.getMessage()+test.getDescription();
    }
}
