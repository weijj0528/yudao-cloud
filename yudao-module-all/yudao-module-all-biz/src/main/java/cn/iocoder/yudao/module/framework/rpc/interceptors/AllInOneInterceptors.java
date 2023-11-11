package cn.iocoder.yudao.module.framework.rpc.interceptors;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type All in one interceptors.
 *
 * @author Administrator
 */
@Slf4j
@Component("allInOneInterceptors")
public class AllInOneInterceptors implements RequestInterceptor {

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        // 修改其他模块rpc调用地址到聚合模块
        requestTemplate.target("http://" + applicationName);
    }
}
