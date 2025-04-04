package com.uomaep.db.configure

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(private val securityInterceptor: SecurityInterceptor): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(securityInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/error", "/assets/**")
            .excludePathPatterns("/user/login", "/user/register", "/ping/**")
    }
}