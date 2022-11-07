package com.tdei.filesvc.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Autowired
    ApplicationProperties applicationProperties;

}
