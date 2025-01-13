package com.examplespringboot.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AppConfig extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin","http://localhost:8080");
        filterChain.doFilter(request,response);
    }
}
// Option 1: WebMvcConfigurer
/*@Configuration
public class AppConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").
                allowCredentials(true)
                .allowedOrigins("http://localhost:8080")
                .allowedHeaders("*")
                .allowedMethods("*");
    }
}

OR

@Configuration
public class AppConfig{
    @Bean
    public WebMvcConfigurer corsFilter(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("*")
                        .allowedHeaders("*");
            }
        };
    }

}

*/

// Option 2: corsFilter

/*@Configuration
public class AppConfig{
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter(){
        UrlBasedCorsConfigurationSource sourse = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(List.of("GET","POST"));
        config.setAllowedHeaders(List.of("*"));

        sourse.registerCorsConfiguration("/user/**",config);

        FilterRegistrationBean bean =  new FilterRegistrationBean<>(new CorsFilter(sourse));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;

    }

}*/

// Option 3: OncePerRequestFilter
/*
@Component
public class AppConfig extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin","http://localhost:8080");
        filterChain.doFilter(request,response);
    }
}
*/

