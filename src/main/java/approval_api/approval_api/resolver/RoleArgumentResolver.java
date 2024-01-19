    package approval_api.approval_api.resolver;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.core.MethodParameter;
    import org.springframework.http.HttpStatus;
    import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
    import org.springframework.web.method.support.ModelAndViewContainer;
    import org.springframework.web.server.ResponseStatusException;

    import approval_api.approval_api.common.UserInfo;
    import approval_api.approval_api.service.AuthService;

    @Component
    public class RoleArgumentResolver implements HandlerMethodArgumentResolver {
        
        @Autowired
        private AuthService authService;

        @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean supports = parameter.getParameterType().equals(String.class) && parameter.hasParameterAnnotation(AuthRole.class);

        return supports;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory) throws Exception {

        String token = webRequest.getHeader("Authorization");
        UserInfo userInfo = authService.extractUserIdFromToken(token);

        AuthRole authRoleAnnotation = parameter.getParameterAnnotation(AuthRole.class);

        if (authRoleAnnotation != null) {
            String expectedRole = authRoleAnnotation.value();

            if (!expectedRole.equals(userInfo.getRole())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "FORBIDDEN");
            }
        }

       try {
         return userInfo.getRole();
       } catch (Exception e) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN,"FORBIDDEN",e);
            }
       }
}

    
