package com.tdei.filesvc.core.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ApiDocConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Bean
    public OpenAPI springOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(applicationProperties.getSwagger().getTitle())
                        .description(applicationProperties.getSwagger().getDescription())
                        .version(applicationProperties.getSwagger().getVersion())
                        .contact(new Contact().name(applicationProperties.getSwagger().getContact().getName())
                                .email(applicationProperties.getSwagger().getContact().getEmail())
                                .url(applicationProperties.getSwagger().getContact().getUrl())));
    }

}
