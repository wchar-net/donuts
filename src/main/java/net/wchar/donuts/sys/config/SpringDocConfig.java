package net.wchar.donuts.sys.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Donuts",
                description = "Donuts API Documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Elijah",
                        email = "northjah@outlook.com",
                        url = "https://outlook.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://wchar.net"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Donuts API Documentation",
                url = "https://outlook.com"
        )
)
/**
 * springdoc 配置
 * @author Elijah
 */
@Configuration
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("donuts")
                .displayName("donuts")
                .pathsToMatch("**", "/**")
                .build();
    }
}
