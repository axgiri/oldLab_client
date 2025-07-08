package github.oldLab.oldLab.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("OldLab API Documentation")
                        .version("1.0.0")
                        .description("Документация REST API для OldLab. Автор: Tais0ft"));
    }
} 