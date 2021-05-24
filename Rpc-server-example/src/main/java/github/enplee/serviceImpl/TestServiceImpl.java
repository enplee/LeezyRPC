package github.enplee.serviceImpl;

import github.enplee.Test;
import github.enplee.TestService;

public class TestServiceImpl implements TestService {
    @Override
    public String test(Test test) {
        return test.getMessage()+test.getDescription();
    }
}
