package approval_api.approval_api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import approval_api.approval_api.resolver.TokenArgumentResolver;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Autowired
    private TokenArgumentResolver tokenArgumentResolver;
  
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenArgumentResolver);
    }

   
}
