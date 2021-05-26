package github.enplee.spring;

import github.enplee.spring.anotation.RpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

@Slf4j
public class CustomScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {

    private static final String SPRING_BEAN_BASE_PACKAGE = "github.enplee.spring";
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private ResourceLoader resourceLoader;
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader  = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes rpcScanAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcService.class.getName()));
        String[] rpcScanPackages = new String[0];
        if(rpcScanAttributes != null) {
            rpcScanPackages = rpcScanAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if(rpcScanPackages.length == 0) {
            rpcScanPackages = new String[]{((StandardAnnotationMetadata) annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }

        CustomScanner rpcServiceScanner = new CustomScanner(registry, RpcService.class);
        CustomScanner componentScanner = new CustomScanner(registry, Component.class);

        int rpcServiceScanCount = rpcServiceScanner.scan(rpcScanPackages);
        log.info("rpcServiceScanner扫描数量 [{}]",rpcServiceScanCount);
        int componentScanCount = componentScanner.scan(SPRING_BEAN_BASE_PACKAGE);
        log.info("componentScanner扫描数量 [{}]",componentScanCount);
    }
}
