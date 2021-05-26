package github.enplee.anotation;


import github.enplee.spring.CustomScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Inherited
public @interface RpcScan {

    String[] basePackage();
}
