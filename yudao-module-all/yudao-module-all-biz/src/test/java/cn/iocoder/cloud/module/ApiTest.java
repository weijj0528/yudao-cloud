package cn.iocoder.cloud.module;

import cn.iocoder.yudao.module.AllServerApplication;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@SpringBootTest(classes = AllServerApplication.class)
public class ApiTest {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Test
    public void apiList() {
        log.info("Api register start……");
        Set<RequestMappingInfo> requestMappingInfos = requestMappingHandlerMapping.getHandlerMethods().keySet();
        List<Api> apiList = new ArrayList<>();
        for (RequestMappingInfo requestMappingInfo : requestMappingInfos) {
            Set<String> methods = requestMappingInfo.getMethodsCondition().getMethods().stream()
                    .map(Enum::name).collect(Collectors.toSet());
            if (methods.isEmpty()) {
                methods.add("*");
            }
            Set<PathPattern> patterns = null;
            if (requestMappingInfo.getPathPatternsCondition() != null) {
                patterns = requestMappingInfo.getPathPatternsCondition().getPatterns();
            }
            for (String method : methods) {
                if (patterns != null) {
                    for (PathPattern pattern : patterns) {
                        // String api = String.format("%s:%s", method, pattern.getPatternString());
                        apiList.add(new Api(method, pattern.getPatternString()));
                    }
                }
            }
        }
        if (!apiList.isEmpty()) {
            // apiList.forEach(System.out::println);
            EasyExcel.write("E:\\Desktop\\API.xlsx", Api.class).sheet("api").doWrite(apiList);
        }
        log.info(String.format("%d api registered", apiList.size()));
    }

    @Data
    public static class Api {
        @ColumnWidth(10)
        @ExcelProperty("Method")
        private String method;
        @ColumnWidth(100)
        @ExcelProperty("Path")
        private String path;

        public Api(String method, String path) {
            this.method = method;
            this.path = path;
        }
    }
}
