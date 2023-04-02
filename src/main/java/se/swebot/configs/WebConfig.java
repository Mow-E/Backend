package se.swebot.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setUseLastModified(true)
                .setCachePeriod(1000)
                .resourceChain(true);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        // No need to specify the filetype in url
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // ViewResolver looks for a View, that is needed so that all URL could lead to React,
        // specifically the index.html file and React will then display the needed content depending on the path
        registry.jsp("/", ".html");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // We allow all hosts
        registry.addMapping("/**")
                .allowedMethods("*");
    }
}
