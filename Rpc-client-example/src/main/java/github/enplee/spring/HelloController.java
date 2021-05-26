package github.enplee.spring;

import github.enplee.Test;
import github.enplee.TestService;
import github.enplee.anotation.RpcRefence;
import org.springframework.stereotype.Component;

@Component
public class HelloController {
    @RpcRefence(group = "1",version = "1")
    public TestService testService;

    public void test() throws InterruptedException {
        String test = this.testService.test(new Test("test", " using annotation"));
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {
            System.out.println(testService.test(new Test("test"+i," using annotation")));

        }
    }

}
